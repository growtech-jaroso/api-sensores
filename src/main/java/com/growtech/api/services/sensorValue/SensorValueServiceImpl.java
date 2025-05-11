package com.growtech.api.services.sensorValue;

import com.growtech.api.entities.Plantation;
import com.growtech.api.entities.Sensor;
import com.growtech.api.entities.SensorValue;
import com.growtech.api.entities.User;
import com.growtech.api.enums.SensorType;
import com.growtech.api.exceptions.CustomException;
import com.growtech.api.repositories.PlantationRepository;
import com.growtech.api.repositories.SensorRepository;
import com.growtech.api.repositories.SensorValueRepository;
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

    Mono<Plantation> plantation = this.plantationRepository.findPlantationByManagersContainingAndIdAndIsDeletedIsFalse(user.getId(), plantationId)
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Plantation with id " + plantationId + " not found")));

    Mono<Sensor> sensor = plantation.flatMap(
      plt -> this.sensorRepository.findSensorByIdAndPlantationIdAndIsDeletedIsFalse(sensorId, plt.getId())
        .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Sensor with id " + sensorId + " not found")))
    );
    return sensor.flatMapMany(
      snr -> this.sensorValueRepository.findAllBySensorIdAndReadingTimestampBetween(sensorId, dateTimePair.getFirst(), dateTimePair.getSecond())
    );
  }

  /**
   * Get all sensor values by plantation id
   *
   * @param plantationId
   * @param checkedDates
   * @return
   */
  @Override
  public Flux<SensorIdProjection> getAllSensorValuesByPlantation(String plantationId, Pair<LocalDateTime, LocalDateTime> checkedDates) {
    Mono<Plantation> plantation = this.plantationRepository.findPlantationsByIdAndIsDeletedIsFalse(plantationId)
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Plantation with id " + plantationId + " not found")));


    return plantation.flatMapMany(
      plt -> this.sensorRepository.findAllByPlantationIdAndIsDeletedIsFalse(plt.getId())
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
  public Flux<Object> getAllSensorValuesByTypeByPlantation(String plantationId, String sensorType, Pair<LocalDateTime, LocalDateTime> checkedDates) {
    Mono<Plantation> plantation = this.plantationRepository.findPlantationsByIdAndIsDeletedIsFalse(plantationId)
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Plantation with id " + plantationId + " not found")));

    return plantation.flatMapMany(
      plt -> this.sensorRepository.findAllByTypeAndPlantationIdAndIsDeletedIsFalse(SensorType.valueOf(sensorType), plt.getId())
    );
  }

  /**
   * Get all sensor values by sensor id and plantation id
   * @param sensorId
   * @param plantationId
   * @param dateTimePair
   * @return
   */
  @Override
  public Flux<SensorValue> getAllSensorValuesBySensorIdAndPlantationId(String sensorId, String plantationId, Pair<LocalDateTime, LocalDateTime> dateTimePair) {
    Mono<Plantation> plantation = this.plantationRepository.findPlantationsByIdAndIsDeletedIsFalse(plantationId)
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Plantation with id " + plantationId + " not found")));

    Mono<Sensor> sensor = plantation.flatMap(
      plt -> this.sensorRepository.findSensorByIdAndPlantationIdAndIsDeletedIsFalse(sensorId, plt.getId())
        .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Sensor with id " + sensorId + " not found")))
    );
    return sensor.flatMapMany(
      snr -> this.sensorValueRepository.findAllBySensorIdAndReadingTimestampBetween(sensorId, dateTimePair.getFirst(), dateTimePair.getSecond())
    );
  }

  /**
   * Get all sensor values by sensor id
   * @param sensorIds
   * @return
   */
  @Override
  public Flux<SensorValue> getAllBySensorIdIn(List<String> sensorIds) {

    return this.sensorValueRepository.findAllBySensorIdIn(sensorIds)
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Sensor with id " + sensorIds + " not found")));
  }

  /**
   * Get all sensor values by sensor id and reading timestamp between
   * @param sensorIds
   * @param timestampAfter
   * @param timestampBefore
   * @return
   */
  @Override
  public Flux<SensorValue> getAllBySensorIdInAndReadingTimestampBetween(List<String> sensorIds, LocalDateTime timestampAfter, LocalDateTime timestampBefore) {

    return this.sensorValueRepository.findAllBySensorIdInAndReadingTimestampBetween(sensorIds, timestampAfter, timestampBefore)
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Sensor with id " + sensorIds + " not found")));
  }


}
