package com.growtech.api.repositories.plantation;

import com.growtech.api.entities.Plantation;
import com.growtech.api.services.plantations.OwnerIdProjection;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface PlantationRepository extends ReactiveMongoRepository<Plantation, String>, PlantationRepositoryCustom {
  Flux<Plantation> findAllByManagersContainingAndIsDeletedIsFalse(String userId);
  Mono<Plantation> findPlantationsByIdAndIsDeletedIsFalse(String plantationId);
  Mono<Boolean> existsPlantationByIdAndIsDeletedIsFalse(String plantationId);
  Mono<Plantation> findPlantationByManagersContainingAndIdAndIsDeletedIsFalse(String userId, String plantationId);
  Mono<Boolean> existsPlantationByOwnerIdAndIsDeletedIsFalse(String ownerId);
  Mono<Boolean> existsPlantationByNameAndOwnerIdAndIsDeletedIsFalse(String name, String ownerId);
  Mono<Boolean> existsPlantationByIdNotAndNameAndOwnerIdAndIsDeletedIsFalse(String plantationId, String name, String ownerId);
  Flux<OwnerIdProjection> findAllByIsDeletedIsFalse();
  Flux<Plantation> findAllByOwnerIdAndIsDeletedIsFalse(String ownerId);
}
