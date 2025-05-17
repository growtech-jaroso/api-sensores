package com.growtech.api.repositories.sensor;

import com.growtech.api.entities.Sensor;
import com.growtech.api.enums.SensorType;
import com.growtech.api.services.sensors.SensorIdProjection;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SensorRepository extends ReactiveMongoRepository<Sensor, String> {
  Flux<Sensor> getAllByPlantationIdAndIsDeletedIsFalse(String plantationId, PageRequest pageRequest);
  Flux<Sensor> getAllByPlantationIdAndIsDeletedIsFalse(String plantationId);
  Mono<Sensor> findByIdAndIsDeletedIsFalse(String id);
  Mono<Long> countAllByPlantationIdAndIsDeletedIsFalse(String plantationId);
  Mono<Boolean> existsByIdAndIsDeletedIsFalse(String id);
  Flux<SensorIdProjection> findAllByPlantationIdAndIsDeletedIsFalse(String plantationId);
  Flux<SensorIdProjection> findAllByTypeAndPlantationIdAndIsDeletedIsFalse(SensorType sensorType, String plantationId);
  Mono<Sensor> findSensorByIdAndPlantationIdAndIsDeletedIsFalse(String id, String plantationId);
}
