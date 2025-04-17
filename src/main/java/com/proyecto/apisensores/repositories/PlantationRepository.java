package com.proyecto.apisensores.repositories;

import com.proyecto.apisensores.entities.Plantation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PlantationRepository extends ReactiveMongoRepository<Plantation, String> {
  Flux<Plantation> findAllBy(PageRequest pageRequest);
  Flux<Plantation> findAllByUsersContaining(String userId, PageRequest pageRequest);
  Mono<Long> countAllByUsersContaining(String userId);
}
