package com.growtech.api.repositories.sensor_value;

import com.growtech.api.entities.SensorValue;
import org.reactivestreams.Publisher;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;

public interface SensorValueRepository extends ReactiveMongoRepository<SensorValue, String> {
  Flux<SensorValue> findAllBySensorIdAndReadingTimestampBetween(String sensorId, LocalDateTime timestampAfter, LocalDateTime timestampBefore);

  Flux<SensorValue> findAllBySensorIdIn(List<String>sensorIds);

  Flux<SensorValue> findAllBySensorIdInAndReadingTimestampBetween(List<String> sensorIds, LocalDateTime timestampAfter, LocalDateTime timestampBefore);

}

