package com.proyecto.apisensores.repositories;

import com.proyecto.apisensores.entities.Sensor;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface SensorRepository extends ReactiveMongoRepository<Sensor, String> {
}
