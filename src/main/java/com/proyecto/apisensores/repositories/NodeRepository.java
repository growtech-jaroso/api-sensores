package com.proyecto.apisensores.repositories;

import com.proyecto.apisensores.entities.Node;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface NodeRepository extends ReactiveMongoRepository<Node, String> {
}
