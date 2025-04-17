package com.proyecto.apisensores.repositories;

import com.proyecto.apisensores.entities.Plantation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantationRepository extends ReactiveMongoRepository<Plantation, String> {
  Flux<Plantation> findByUserId(String userId, Pageable pageable);
}
