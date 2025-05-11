package com.growtech.api.repositories.plantation;

import com.growtech.api.entities.Plantation;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlantationRepositoryCustom {
  /**
   * Find all plantations by filters and is deleted is false
   * @param name name of the plantation
   * @param country country of the plantation
   * @param province province of the plantation
   * @param city city of the plantation
   * @param type type of the plantation
   * @param pageRequest page request for pagination
   * @return a flux of plantations
   */
  Flux<Plantation> findAllByFiltersAndIsDeletedIsFalse(
    String name,
    String country,
    String province,
    String city,
    String type,
    PageRequest pageRequest
  );

  /**
   * Count all plantations by filters and is deleted is false
   * @param name name of the plantation
   * @param country country of the plantation
   * @param province province of the plantation
   * @param city city of the plantation
   * @param type type of the plantation
   * @return a mono of the count of plantations
   */
  Mono<Long> countAllByFiltersAndIsDeletedIsFalse(
    String name,
    String country,
    String province,
    String city,
    String type
  );

  /**
   * Find all plantations by managers containing and filters and is deleted is false
   * @param userId user id of the user
   * @param name name of the plantation
   * @param country country of the plantation
   * @param province province of the plantation
   * @param city city of the plantation
   * @param type type of the plantation
   * @param pageRequest page request for pagination
   * @return a flux of plantations
   */
  Flux<Plantation> findAllByManagersContainingAndFiltersAndIsDeletedIsFalse(
    String userId,
    String name,
    String country,
    String province,
    String city,
    String type,
    PageRequest pageRequest
  );

  /**
   * Count all plantations by managers containing and filters and is deleted is false
   * @param userId user id of the user
   * @param name name of the plantation
   * @param country country of the plantation
   * @param province province of the plantation
   * @param city city of the plantation
   * @param type  type of the plantation
   * @return a mono of the count of plantations
   */
  Mono<Long> countAllByManagersContainingAndFiltersAndIsDeletedIsFalse(
    String userId,
    String name,
    String country,
    String province,
    String city,
    String type
  );

  /**
   * Find all plantations by filters and has alerts and is deleted is false
   * @param name name of the plantation
   * @param country country of the plantation
   * @param province province of the plantation
   * @param city city of the plantation
   * @param type type of the plantation
   * @param hasAlerts has alerts of the plantation
   * @param pageRequest page request for pagination
   * @return a flux of plantations
   */
  Flux<Plantation> findAllByFiltersAndHasAlertsAndIsDeletedIsFalse(
    String name,
    String country,
    String province,
    String city,
    String type,
    Boolean hasAlerts,
    PageRequest pageRequest
  );

  /**
   * Count all plantations by filters and has alerts and is deleted is false
   * @param name name of the plantation
   * @param country country of the plantation
   * @param province province of the plantation
   * @param city city of the plantation
   * @param type type of the plantation
   * @param hasAlerts has alerts of the plantation
   * @return a mono of the count of plantations
   */
  Mono<Long> countAllByFiltersAndHasAlertsAndIsDeletedIsFalse(
    String name,
    String country,
    String province,
    String city,
    String type,
    Boolean hasAlerts
  );

  /**
   * Find all plantations by managers containing and filters and has alerts and is deleted is false
   * @param userId user id of the user
   * @param name name of the plantation
   * @param country country of the plantation
   * @param province province of the plantation
   * @param city city of the plantation
   * @param type type of the plantation
   * @param hasAlerts has alerts of the plantation
   * @param pageRequest page request for pagination
   * @return a flux of plantations
   */
  Flux<Plantation> findAllByManagersContainingAndFiltersAndHasAlertsAndIsDeletedIsFalse(
    String userId,
    String name,
    String country,
    String province,
    String city,
    String type,
    Boolean hasAlerts,
    PageRequest pageRequest
  );

  /**
   * Count all plantations by managers containing and filters and has alerts and is deleted is false
   * @param userId user id of the possible manager
   * @param name name of the plantation
   * @param country country of the plantation
   * @param province province of the plantation
   * @param city city of the plantation
   * @param type type of the plantation
   * @param hasAlerts has alerts of the plantation
   * @return a mono of the count of plantations
   */
  Mono<Long> countByManagersContainingAndFiltersAndHasAlertsAndIsDeletedIsFalse(
    String userId,
    String name,
    String country,
    String province,
    String city,
    String type,
    Boolean hasAlerts
  );
}
