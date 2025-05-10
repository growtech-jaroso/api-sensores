package com.growtech.api.repositories;

import com.growtech.api.entities.Plantation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PlantationRepository extends ReactiveMongoRepository<Plantation, String> {
  Flux<Plantation> findAllByNameContainsIgnoreCaseAndIsDeletedIsFalse(String name, PageRequest pageRequest);
  Flux<Plantation> findAllByManagersContainingAndIsDeletedIsFalse(String userId);
  Flux<Plantation> findAllByManagersContainingAndNameContainsIgnoreCaseAndIsDeletedIsFalse(String userId, String name, PageRequest pageRequest);
  Mono<Boolean> existsPlantationByNameAndOwnerIdAndIsDeletedIsFalse(String name, String ownerId);
  Mono<Boolean> existsPlantationByIdNotAndNameAndOwnerIdAndIsDeletedIsFalse(String id, String name, String ownerId);
  Mono<Long> countAllByManagersContainingAndNameContainsIgnoreCaseAndIsDeletedIsFalse(String userId, String name);
  Mono<Plantation> findPlantationsByIdAndIsDeletedIsFalse(String plantationId);
  Mono<Boolean> existsPlantationByIdAndIsDeletedIsFalse(String plantationId);
  Mono<Long> countAllByNameContainsIgnoreCaseAndIsDeletedIsFalse(String name);
  Mono<Boolean> existsPlantationByOwnerIdAndIsDeletedIsFalse(String ownerId);
}
