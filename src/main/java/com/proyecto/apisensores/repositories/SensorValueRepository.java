package com.proyecto.apisensores.repositories;

import com.proyecto.apisensores.entities.SensorValue;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface SensorValueRepository extends ReactiveMongoRepository<SensorValue, String> {
}
