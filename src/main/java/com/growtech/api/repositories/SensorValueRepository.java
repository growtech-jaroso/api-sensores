package com.growtech.api.repositories;

import com.growtech.api.entities.SensorValue;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface SensorValueRepository extends ReactiveMongoRepository<SensorValue, String> {
}
