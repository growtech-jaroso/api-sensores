package com.growtech.api.services.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.growtech.api.dtos.mqtt.PlantationStatusDto;
import com.growtech.api.dtos.mqtt.SensorReadingDto;
import com.growtech.api.entities.SensorValue;
import com.growtech.api.enums.ActuatorStatus;
import com.growtech.api.enums.PlantationStatus;
import com.growtech.api.repositories.plantation.PlantationRepository;
import com.growtech.api.repositories.sensor.SensorRepository;
import com.growtech.api.repositories.sensor_value.SensorValueRepository;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;

@Service
@Slf4j
public class MqttMessageServiceImpl implements MqttMessageService {

  private final PlantationRepository plantationRepository;
  private final SensorRepository sensorRepository;
  private final SensorValueRepository sensorValueRepository;
  private final ObjectMapper objectMapper;
  private MqttClient mqttClient;

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
    this.mqttClient = null;
  }

  @Override
  public void setMqttClient(MqttClient mqttClient) {
    this.mqttClient = mqttClient;
  }

  @Override
  public void processMessage(String topic, MqttMessage message) {
    if (topic.contains("event/reading")) this.processSensorReading(topic, message);
    else if (topic.contains("event/status")) this.processPlantationStatus(topic, message);
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

    // Check if the plantation and sensor exist
    this.checkIfPlantationExists(plantationId)
      .flatMap(exists -> {
        if (!exists) {
          log.error("Plantation with ID '{}' does not exist", plantationId);
          return Mono.empty(); // Stop the flow without error
        }
        return this.checkIfSensorExists(sensorId);
      })
      .flatMap(sensorExists -> {
        if (!sensorExists) {
          log.error("Sensor with ID '{}' does not exist", sensorId);
          return Mono.empty(); // Stop the flow without error
        }

        // Deserialize the message to SensorReadingDto
        SensorReadingDto dto = this.getObjectFromJson(message.toString(), SensorReadingDto.class);
        if (dto == null) {
          log.error("SensorReadingDto is null or malformed for message: {}", message);
          return Mono.empty(); // Stop the flow without error
        }

        // Convert the DTO to SensorValue
        SensorValue value = dto.toSensorValue(sensorId);
        if (value == null) {
          log.error("Failed to convert SensorReadingDto to SensorValue");
          return Mono.empty(); // Stop the flow without error
        }

        // Save the SensorValue to the database
        return this.sensorValueRepository.save(value)
          .doOnSuccess(saved -> log.info("Sensor value saved: {}", saved));
      })
      .doOnError(e -> log.error("Unexpected error during processing: {}", e.getMessage()))
      .subscribe(); // no onErrorConsumer
  }



  private void processPlantationStatus(String topic, MqttMessage message) {
    log.info("Processing device status message: {}", message);
    String plantationId = this.getPlantationId(topic);

    // Check if the plantation exists
    this.plantationRepository.findPlantationsByIdAndIsDeletedIsFalse(plantationId)
      .flatMap(plantation -> {
        if (plantation == null) {
          log.error("Plantation with ID '{}' does not exist", plantationId);
          return Mono.empty(); // Stop the flow without error
        }

        PlantationStatusDto plantationStatusDto = this.getObjectFromJson(message.toString(), PlantationStatusDto.class);

        if (plantationStatusDto == null) {
          log.error("PlantationStatusDto is null or malformed for message: {}", message);
          return Mono.empty(); // Stop the flow without error
        }

        // Convert the DTO to PlantationStatus
        PlantationStatus status = plantationStatusDto.toPlantationStatus();
        if (status == null) {
          log.error("Failed to convert PlantationStatusDto to PlantationStatus");
          return Mono.empty(); // Stop the flow without error
        }

        // Set the status of the plantation
        plantation.setStatus(status);

        // Save the plantation to the database
        return this.plantationRepository.save(plantation)
          .doOnSuccess(saved -> log.info("Plantation status saved: {}", saved));
      })
      .doOnError(e -> log.error("Unexpected error during processing: {}", e.getMessage()))
      .subscribe(); // no onErrorConsumer
  }

  public void publishActuatorStatus(String plantationId, String sensorId, ActuatorStatus status) {
    try {
      String topic = String.format("plantation/%s/actuator/%s/status", plantationId, sensorId);

      Map<String, String> payloadMap = Map.of(
        "status", status.name(),
        "timestamp", Instant.now().toString()
      );

      String payload = objectMapper.writeValueAsString(payloadMap);

      MqttMessage mqttMessage = new MqttMessage(payload.getBytes(StandardCharsets.UTF_8));
      mqttMessage.setQos(1);

      log.info("Publishing actuator status to MQTT topic '{}': {}", topic, payload);
      this.mqttClient.publish(topic, mqttMessage);

    } catch (Exception e) {
      log.error("Error publishing actuator status to MQTT", e);
    }
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
