package com.growtech.api.repositories.plantation;

import com.growtech.api.entities.Plantation;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlantationRepositoryCustom {
  /**
   * Find all plantations by filters and is deleted is false
   * @param search search filter of the plantation
   * @param pageRequest page request for pagination
   * @return a flux of plantations
   */
  Flux<Plantation> findAllByFiltersAndIsDeletedIsFalse(
    String search,
    PageRequest pageRequest
  );

  /**
   * Count all plantations by filters and is deleted is false
   * @param search search filter of the plantation
   * @return a mono of the count of plantations
   */
  Mono<Long> countAllByFiltersAndIsDeletedIsFalse(String search);

  /**
   * Find all plantations by managers containing and filters and is deleted is false
   * @param userId user id of the user
   * @param search search filter of the plantation
   * @param pageRequest page request for pagination
   * @return a flux of plantations
   */
  Flux<Plantation> findAllByManagersContainingAndFiltersAndIsDeletedIsFalse(
    String userId,
    String search,
    PageRequest pageRequest
  );

  /**
   * Count all plantations by managers containing and filters and is deleted is false
   * @param userId user id of the user
   * @param search search filter of the plantation
   * @return a mono of the count of plantations
   */
  Mono<Long> countAllByManagersContainingAndFiltersAndIsDeletedIsFalse(
    String userId,
    String search
  );

  /**
   * Find all plantations by filters and has alerts and is deleted is false
   * @param search search filter of the plantation
   * @param plantationStatus plantation status of the plantation
   * @param pageRequest page request for pagination
   * @return a flux of plantations
   */
  Flux<Plantation> findAllByFiltersAndPlantationStatusAndIsDeletedIsFalse(
    String search,
    String plantationStatus,
    PageRequest pageRequest
  );

  /**
   * Count all plantations by filters and has alerts and is deleted is false
   * @param search search filter of the plantation
   * @param plantationStatus plantation status of the plantation
   * @return a mono of the count of plantations
   */
  Mono<Long> countAllByFiltersAndPlantationStatusAndIsDeletedIsFalse(
    String search,
    String plantationStatus
  );

  /**
   * Find all plantations by managers containing and filters and has alerts and is deleted is false
   * @param userId user id of the user
   * @param search search filter of the plantation
   * @param plantationStatus plantation status of the plantation
   * @param pageRequest page request for pagination
   * @return a flux of plantations
   */
  Flux<Plantation> findAllByManagersContainingAndFiltersAndPlantationStatusAndIsDeletedIsFalse(
    String userId,
    String search,
    String plantationStatus,
    PageRequest pageRequest
  );

  /**
   * Count all plantations by managers containing and filters and has alerts and is deleted is false
   * @param userId user id of the possible manager
   * @param search search filter of the plantation
   * @param plantationStatus plantation status of the plantation
   * @return a mono of the count of plantations
   */
  Mono<Long> countByManagersContainingAndFiltersAndPlantationStatusAndIsDeletedIsFalse(
    String userId,
    String search,
    String plantationStatus
  );
}
