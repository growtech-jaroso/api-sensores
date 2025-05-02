package com.growtech.api.repositories;

import com.growtech.api.entities.Sensor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SensorRepository extends ReactiveMongoRepository<Sensor, String> {
  Flux<Sensor> getAllByPlantationIdAndIsDeletedIsFalse(String plantationId, PageRequest pageRequest);
  Mono<Long> countAllByPlantationIdAndIsDeletedIsFalse(String plantationId);
}
