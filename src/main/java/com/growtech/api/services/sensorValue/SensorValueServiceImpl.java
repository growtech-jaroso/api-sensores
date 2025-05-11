package com.growtech.api.services.sensorValue;

import com.growtech.api.entities.Plantation;
import com.growtech.api.entities.Sensor;
import com.growtech.api.entities.SensorValue;
import com.growtech.api.entities.User;
import com.growtech.api.exceptions.CustomException;
import com.growtech.api.repositories.PlantationRepository;
import com.growtech.api.repositories.SensorRepository;
import com.growtech.api.repositories.SensorValueRepository;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

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
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Plantation not found")));

    Mono<Sensor> sensor = plantation.flatMap(
      plt -> this.sensorRepository.findSensorByIdAndPlantationIdAndIsDeletedIsFalse(sensorId, plt.getId())
        .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Sensor not found")))
    );

    return sensor.flatMapMany(
      s -> this.sensorValueRepository.findAllBySensorIdAndReadingTimestampBetween(sensorId, dateTimePair.getFirst(), dateTimePair.getSecond())
    );
  }

}
