package com.growtech.api.repositories;

import com.growtech.api.entities.SensorValue;
import com.growtech.api.enums.SensorType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

public interface SensorValueRepository extends ReactiveMongoRepository<SensorValue, String> {
  Flux<SensorValue> findAllBySensorIdAndReadingTimestampBetween(String sensorId, LocalDateTime timestampAfter, LocalDateTime timestampBefore);

  Flux<SensorValue> findAllBySensorIdIn(List<String>sensorIds);

  Flux<SensorValue> findAllBySensorIdInAndReadingTimestampBetween(List<String> sensorIds, LocalDateTime timestampAfter, LocalDateTime timestampBefore);

}

