package com.growtech.api.services.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.growtech.api.dtos.mqtt.SensorReadingDto;
import com.growtech.api.entities.SensorValue;
import com.growtech.api.repositories.plantation.PlantationRepository;
import com.growtech.api.repositories.sensor.SensorRepository;
import com.growtech.api.repositories.sensor_value.SensorValueRepository;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@Slf4j
public class MqttMessageServiceImpl implements MqttMessageService {

  private final PlantationRepository plantationRepository;
  private final SensorRepository sensorRepository;
  private final SensorValueRepository sensorValueRepository;
  private final ObjectMapper objectMapper;

  public MqttMessageServiceImpl(
    PlantationRepository plantationRepository,
    SensorRepository sensorRepository,
    SensorValueRepository sensorValueRepository,
    ObjectMapper objectMapper
  ) {
    this.plantationRepository = plantationRepository;
    this.sensorRepository = sensorRepository;
    this.sensorValueRepository = sensorValueRepository;
    this.objectMapper = objectMapper;
  }

  @Override
  public void processMessage(String topic, MqttMessage message) {
    if (topic.contains("event/reading")) this.processSensorReading(topic, message);
    else if (topic.contains("event/status")) this.processDeviceStatus(topic, message);
    else log.warn("Unknown topic: {}", topic);
  }

  /**
   * Process the sensor reading message.
   * @param topic The topic of the message
   * @param message The message to process as a MqttMessage
   */
  private void processSensorReading(String topic, MqttMessage message) {
    log.info("Processing sensor reading message: {}", message);
    String plantationId = this.getPlantationId(topic);
    String sensorId = this.getSensorId(topic);

    // Check if the plantation exists in the database
    if (!this.checkIfPlantationExists(plantationId).block()) {
      log.warn("Plantation with ID {} does not exist", plantationId);
      return;
    }

    // Check if the sensor exists in the database
    if (!this.checkIfSensorExists(sensorId).block()) {
      log.warn("Sensor with ID {} does not exist", sensorId);
      return;
    }

    // Deserialize the message to a SensorReadingDto
    SensorReadingDto sensorReadingDto = this.getObjectFromJson(message.toString(), SensorReadingDto.class);

    // Check if the deserialization was successful
    if (sensorReadingDto == null) {
      log.warn("Failed to parse sensor reading DTO from message: {}", message);
      return;
    }

    // Convert the DTO to a SensorValue entity
    SensorValue sensorValue = sensorReadingDto.toSensorValue(sensorId);

    // Check if the conversion was successful
    if (sensorValue == null) {
      log.warn("Failed to convert sensor reading DTO to SensorValue entity");
      return;
    }

    // Save the sensor value to the database
    this.sensorValueRepository.save(sensorValue)
      .doOnSuccess(savedSensorValue -> log.info("Sensor value saved: {}", savedSensorValue))
      .doOnError(error -> log.error("Error saving sensor value", error))
      .subscribe();
  }

  private void processDeviceStatus(String topic, MqttMessage message) {
    log.info("Processing device status message: {}", message);
    String plantationId = this.getPlantationId(topic);

    // TODO: Process the device status
    Boolean plantationExists = this.checkIfPlantationExists(plantationId).block();
  }

  /**
   * Extracts the plantation ID
   * @param topic The topic of the message
   * @return Plantation ID from the topic
   */
  private String getPlantationId(String topic) {
    String[] topicParts = topic.split("/");
    return topicParts[1];
  }

  /**
   * Extracts the sensor ID
   * @param topic The topic of the message
   * @return Sensor ID from the topic
   */
  private String getSensorId(String topic) {
    String[] topicParts = topic.split("/");
    return topicParts[3];
  }

  /**
   * Checks if the plantation exists in the database.
   * @param plantationId The ID of the plantation to check
   * @return A Mono that completes when the check is done
   */
  private Mono<Boolean> checkIfPlantationExists(String plantationId) {
    // Check if the plantation exists in the database
    return this.plantationRepository.existsPlantationByIdAndIsDeletedIsFalse(plantationId)
      .switchIfEmpty(Mono.just(false));
  }

  /**
   * Checks if the sensor exists in the database.
   * @param sensorId The ID of the sensor to check
   * @return A Mono that completes when the check is done
   */
  private Mono<Boolean> checkIfSensorExists(String sensorId) {
    // Check if the sensor exists in the database
    return this.sensorRepository.existsByIdAndIsDeletedIsFalse(sensorId)
      .switchIfEmpty(Mono.just(false));
  }

  private <T> T getObjectFromJson(String json, Class<T> clazz) {
    try {
      return objectMapper.readValue(json, clazz);
    } catch (Exception e) {
      return null;
    }
  }
}
