package com.growtech.api.services.sensor_value;

import com.growtech.api.entities.Plantation;
import com.growtech.api.entities.Sensor;
import com.growtech.api.entities.SensorValue;
import com.growtech.api.entities.User;
import com.growtech.api.enums.SensorType;
import com.growtech.api.exceptions.CustomException;
import com.growtech.api.repositories.plantation.PlantationRepository;
import com.growtech.api.repositories.sensor.SensorRepository;
import com.growtech.api.repositories.sensor_value.SensorValueRepository;
import com.growtech.api.services.sensors.SensorIdProjection;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SensorValueServiceImpl implements SensorValueService {

  private final SensorValueRepository sensorValueRepository;
  private final PlantationRepository plantationRepository;
  private final SensorRepository sensorRepository;

  public SensorValueServiceImpl(SensorValueRepository sensorValueRepository, PlantationRepository plantationRepository, SensorRepository sensorRepository) {
    this.sensorValueRepository = sensorValueRepository;
    this.plantationRepository = plantationRepository;
    this.sensorRepository = sensorRepository;
  }

  @Override
  public Flux<SensorValue> getAllSensorValuesBySensorId(User user, String sensorId, String plantationId, Pair<LocalDateTime, LocalDateTime> dateTimePair) {

    Mono<Plantation> plantation = user.canViewAnything()
      ? this.plantationRepository.findPlantationsByIdAndIsDeletedIsFalse(plantationId)
        .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Plantation with id " + plantationId + " not found")))
      : this.plantationRepository.findPlantationByManagersContainingAndIdAndIsDeletedIsFalse(user.getId(), plantationId)
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Plantation not found")));

    Mono<Sensor> sensor = plantation.flatMap(
      plt -> this.sensorRepository.findSensorByIdAndPlantationIdAndIsDeletedIsFalse(sensorId, plt.getId())
        .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Sensor not found")))
    );

    return sensor.flatMapMany(
      s -> this.sensorValueRepository.findAllBySensorIdAndReadingTimestampBetween(sensorId, dateTimePair.getFirst(), dateTimePair.getSecond())
    );
  }

  /**
   * Get all sensor values by sensor type and plantation id
   * @param plantationId
   * @param sensorType
   * @param checkedDates
   * @return
   */
  @Override
  public Flux<SensorValue> getAllSensorValuesByTypeByPlantation(User user, String plantationId, SensorType sensorType, Pair<LocalDateTime, LocalDateTime> checkedDates) {
    Mono<Plantation> plantation = user.canViewAnything()
      ? this.plantationRepository.findPlantationsByIdAndIsDeletedIsFalse(plantationId)
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Plantation with id " + plantationId + " not found")))
      : this.plantationRepository.findPlantationByManagersContainingAndIdAndIsDeletedIsFalse(user.getId(), plantationId)
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Plantation not found")));

    return plantation.flatMapMany(
      plt -> {
        if (!plt.getManagers().contains(user.getId())) {
          return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "FORBIDDEN"));
        }
        return this.sensorRepository.findAllByTypeAndPlantationIdAndIsDeletedIsFalse(sensorType, plt.getId())
          .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Sensor with type " + sensorType + " not found")))
          .collectList()
          .flatMap(
            sensors -> {
              if (sensors.isEmpty()) {
                return Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Sensor with type " + sensorType + " not found"));
              }
              return Mono.just(sensors.stream()
              .map(SensorIdProjection::getId)
              .toList());
            })
          .flatMapMany(sensors -> this.sensorValueRepository.findAllBySensorIdInAndReadingTimestampBetween(sensors, checkedDates.getFirst(), checkedDates.getSecond()));
      });
  }


}
