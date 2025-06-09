package com.growtech.api.services.sensors;

import com.growtech.api.dtos.requests.SensorDto;
import com.growtech.api.entities.Plantation;
import com.growtech.api.entities.Sensor;
import com.growtech.api.entities.User;
import com.growtech.api.enums.ActuatorStatus;
import com.growtech.api.enums.DeviceType;
import com.growtech.api.exceptions.CustomException;
import com.growtech.api.repositories.plantation.PlantationRepository;
import com.growtech.api.repositories.sensor.SensorRepository;
import com.growtech.api.services.mqtt.MqttMessageServiceImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

@Service
public class SensorServiceImpl implements SensorService {

  private final SensorRepository sensorRepository;
  private final PlantationRepository plantationRepository;
  private final MqttMessageServiceImpl mqttMessageService;

  public SensorServiceImpl(SensorRepository sensorRepository, PlantationRepository plantationRepository, MqttMessageServiceImpl mqttMessageService) {
    this.sensorRepository = sensorRepository;
    this.plantationRepository = plantationRepository;
    this.mqttMessageService = mqttMessageService;
  }

  @Override
  public Mono<Tuple2<List<Sensor>, Long>> getSensorsByPlantationPaginated(User user, String plantationId, PageRequest pageRequest) {
    // Check if plantation exists
    Mono<Plantation> plantation = this.plantationRepository.findPlantationsByIdAndIsDeletedIsFalse(plantationId)
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Plantation not found")));

    return plantation
      .flatMap(pl -> {
        // Check if the user is associated with the plantation
        if (!pl.getOwnerId().contains(user.getId()) && !user.canViewAnything()) {
          return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "Forbidden"));
        }

        // Get the sensors by plantation id
        return Mono.zip(
          this.sensorRepository.getAllByPlantationIdAndIsDeletedIsFalse(plantationId, pageRequest).collectList(),
          this.sensorRepository.countAllByPlantationIdAndIsDeletedIsFalse(plantationId)
        );
      });
  }

  @Override
  public Mono<Sensor> createSensor(SensorDto sensorDto, String plantationId) {
    // Check if plantation exists
    return this.plantationRepository.existsPlantationByIdAndIsDeletedIsFalse(plantationId)
      .flatMap(exists -> {
        if (!exists) {
          return Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Plantation not found"));
        }
        // Create a new sensor object
        Sensor sensor = new Sensor(sensorDto, plantationId);
        // Save the sensor
        return this.sensorRepository.save(sensor);
      });
  }

  @Override
  public Mono<Sensor> createActuatorSensor(String plantationId) {
    // Check if plantation exists
    return this.plantationRepository.existsPlantationByIdAndIsDeletedIsFalse(plantationId)
      .flatMap(exists -> {
        if (!exists) {
          return Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Plantation not found"));
        }
        // Create a new sensor object
        Sensor sensor = new Sensor(plantationId);
        // Save the sensor
        return this.sensorRepository.save(sensor);
      });
  }

  @Override
  public Mono<Sensor> updateActuatorSensor(String sensorId, String plantationId, ActuatorStatus status) {

    return plantationRepository.findPlantationsByIdAndIsDeletedIsFalse(plantationId)
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Plantation not found")))
      .flatMap(plantation -> sensorRepository.findByIdAndIsDeletedIsFalse(sensorId))
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Sensor not found")))
      .flatMap(sensor -> {
        if (sensor.getDeviceType() != DeviceType.ACTUATOR) {
          return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Sensor is not an actuator"));
        }

        if (status == null) {
          return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Invalid status value"));
        }

        sensor.setStatus(status);

        return sensorRepository.save(sensor)
          .doOnSuccess(savedSensor -> {
            // Publicar el cambio de estado via MQTT
            mqttMessageService.publishActuatorStatus(plantationId, sensorId, status);
          });
      });
  }



  @Override
  public Mono<String> deleteSensor(String sensorId, String plantationId) {
    // Check if plantation exists, if not exists throw not found exception
    Mono<Plantation> plantation = this.plantationRepository.findPlantationsByIdAndIsDeletedIsFalse(plantationId)
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Plantation not found")));

    return plantation
      // Find the sensor by id and if not found throw not found exception
      .flatMap(pl -> this.sensorRepository.findByIdAndIsDeletedIsFalse(sensorId))
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Sensor not found")))
      // Set the sensor as deleted
      .flatMap(sensor -> {
        sensor.softDelete();
        return this.sensorRepository.save(sensor);
      })
      .then(Mono.just("Sensor deleted successfully"));
  }
}
