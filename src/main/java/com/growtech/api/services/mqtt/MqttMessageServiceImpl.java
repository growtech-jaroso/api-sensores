package com.growtech.api.services.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.growtech.api.dtos.mqtt.SensorValueDto;
import com.growtech.api.entities.SensorValue;
import com.growtech.api.repositories.plantation.PlantationRepository;
import com.growtech.api.repositories.sensor.SensorRepository;
import com.growtech.api.repositories.sensor_value.SensorValueRepository;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
    log.info("Processing message for topic {}", topic);
    switch (topic) {
      case "plantation/+/sensor/+/event/reading":
        processSensorReading(topic, message);
        break;
      case "plantation/+/event/status":
        processDeviceStatus(topic, message);
        break;
    }
  }

  /**
   * Process the sensor reading message.
   * @param topic The topic of the message
   * @param message The message to process as a MqttMessage
   */
  private void processSensorReading(String topic, MqttMessage message) {
    System.out.println("Processing sensor reading: " + message.toString());
    String plantationId = this.getPlantationId(topic);
    String sensorId = this.getSensorId(topic);

    this.checkIfPlantationExists(plantationId)
      .then(this.checkIfSensorExists(sensorId))
      .then(Mono.just(this.getObjectFromJson(message.toString(), SensorValueDto.class)))
      .map(sensorValueDto -> sensorValueDto.toSensorValue(sensorId))
      .flatMap(this.sensorValueRepository::save)
      .subscribe(sensorValue -> log.info("Sensor value saved: {}", sensorValue));
  }

  private void processDeviceStatus(String topic, MqttMessage message) {
    String plantationId = this.getPlantationId(topic);

    // TODO: Process the device status
    this.checkIfPlantationExists(plantationId)
      .subscribe(unused -> System.out.println("Device status: " + message.toString()));
//      .then(this.plantationRepository.updateDeviceStatus(plantationId, message));
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
  private Mono<Void> checkIfPlantationExists(String plantationId) {
    // Check if the plantation exists in the database
    Mono<Boolean> existsPlantation = this.plantationRepository.existsPlantationByIdAndIsDeletedIsFalse(plantationId)
      .switchIfEmpty(Mono.just(false));

    // If the plantation does not exist, log an error and return an empty Mono
    return existsPlantation.flatMap(existsPlantationResult -> {
      if (!existsPlantationResult) {
        log.warn("Plantation with ID {} does not exist", plantationId);
      }

      return Mono.empty();
    });
  }

  /**
   * Checks if the sensor exists in the database.
   * @param sensorId The ID of the sensor to check
   * @return A Mono that completes when the check is done
   */
  private Mono<Void> checkIfSensorExists(String sensorId) {
    // Check if the plantation exists in the database
    Mono<Boolean> existsPlantation = this.sensorRepository.existsByIdAndIsDeletedIsFalse(sensorId)
      .switchIfEmpty(Mono.just(false));

    // If the plantation does not exist, log an error and return an empty Mono
    return existsPlantation.flatMap(existsPlantationResult -> {
      if (!existsPlantationResult) {
        log.warn("Sensor with ID {} does not exist", sensorId);
      }

      return Mono.empty();
    });
  }

  private <T> T getObjectFromJson(String json, Class<T> clazz) {
    try {
      return objectMapper.readValue(json, clazz);
    } catch (Exception e) {
      log.error("Error parsing JSON: {}", e.getMessage());
      throw new RuntimeException();
    }
  }
}
