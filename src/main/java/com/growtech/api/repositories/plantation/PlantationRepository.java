package com.growtech.api.repositories.plantation;

import com.growtech.api.entities.Plantation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PlantationRepository extends ReactiveMongoRepository<Plantation, String> {
  Flux<Plantation> findAllByIsDeletedIsFalse(PageRequest pageRequest);
  Flux<Plantation> findAllByManagersContainingAndIsDeletedIsFalse(String userId);
  Flux<Plantation> findAllByManagersContainingAndIsDeletedIsFalse(String userId, PageRequest pageRequest);
  Mono<Boolean> existsPlantationByNameAndIsDeletedIsFalse(String name);
  Mono<Long> countAllByManagersContainingAndIsDeletedIsFalse(String userId);
  Mono<Plantation> findPlantationsByIdAndIsDeletedIsFalse(String plantationId);
  Mono<Boolean> existsPlantationByIdAndIsDeletedIsFalse(String plantationId);
  Mono<Long> countAllByIsDeletedIsFalse();
  Mono<Plantation> findPlantationByManagersContainingAndIdAndIsDeletedIsFalse(String userId, String plantationId);
  Mono<Boolean> existsPlantationByOwnerIdAndIsDeletedIsFalse(String ownerId);
}
