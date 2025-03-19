package com.proyecto.apisensores.repositories;

import com.proyecto.apisensores.entities.NodeCoordinate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface NodeCoordinateRepository extends ReactiveMongoRepository<NodeCoordinate, String> {
}
