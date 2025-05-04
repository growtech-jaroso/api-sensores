package com.growtech.api.repositories;

import com.growtech.api.entities.SensorValue;
import com.growtech.api.enums.SensorType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface SensorValueRepository extends ReactiveMongoRepository<SensorValue, String> {
  Flux<SensorValue> findBySensorId(String sensorId);

  Flux<SensorValue> findByValue(Double value);

  Flux<SensorValue> findBySensorType(SensorType sensorType);

  Flux<SensorValue> findBySensorTypeAndTimestampBetween(SensorType sensorType, LocalDateTime timestampAfter, LocalDateTime timestampBefore);

  Flux<SensorValue> findByTimestampBetween(LocalDateTime timestamp, LocalDateTime timestamp2);

  Flux<SensorValue> findByIdAndTimestampBetween(String id, LocalDateTime timestampAfter, LocalDateTime timestampBefore);

  Flux<SensorValue> findBySensorIdOrderByTimestamp(String sensorId);
}

