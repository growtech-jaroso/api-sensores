package com.proyecto.apisensores.repositories;

import com.proyecto.apisensores.entities.PlantationCoordinate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PlantationCoordinateRepository extends ReactiveMongoRepository<PlantationCoordinate, String> {
}
