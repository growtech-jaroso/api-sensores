package com.growtech.api.repositories.plantation;

import com.growtech.api.entities.Plantation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class PlantationRepositoryCustomImpl implements PlantationRepositoryCustom {

  private final ReactiveMongoTemplate reactiveMongoTemplate;

  public PlantationRepositoryCustomImpl(ReactiveMongoTemplate reactiveMongoTemplate) {
    this.reactiveMongoTemplate = reactiveMongoTemplate;
  }

  @Override
  public Flux<Plantation> findAllByFiltersAndIsDeletedIsFalse(String search, PageRequest pageRequest) {
    Criteria criteria = getCriteriaForFiltersAndIsDeletedIsFalse(search);

    Query query = new Query(criteria);
    query.with(pageRequest); // Apply pagination

    return reactiveMongoTemplate.find(query, Plantation.class);
  }

  @Override
  public Mono<Long> countAllByFiltersAndIsDeletedIsFalse(String search) {
    Criteria criteria = getCriteriaForFiltersAndIsDeletedIsFalse(search);

    Query query = new Query(criteria);
    return reactiveMongoTemplate.count(query, Plantation.class);
  }

  @Override
  public Flux<Plantation> findAllByManagersContainingAndFiltersAndIsDeletedIsFalse(String userId, String search, PageRequest pageRequest) {
    Criteria criteria = getCriteriaForManagersContainingAndFiltersAndIsDeletedIsFalse(userId, search);

    Query query = new Query(criteria);
    query.with(pageRequest); // Apply pagination

    return reactiveMongoTemplate.find(query, Plantation.class);
  }

  @Override
  public Mono<Long> countAllByManagersContainingAndFiltersAndIsDeletedIsFalse(String userId, String search) {
    Criteria criteria = getCriteriaForManagersContainingAndFiltersAndIsDeletedIsFalse(userId, search);

    Query query = new Query(criteria);
    return reactiveMongoTemplate.count(query, Plantation.class);
  }

  @Override
  public Flux<Plantation> findAllByFiltersAndPlantationStatusAndIsDeletedIsFalse(String search, String status, PageRequest pageRequest) {
    Criteria criteria = getCriteriaForFiltersAndHasAlertsAndIsDeletedIsFalse(search, status);

    Query query = new Query(criteria);
    query.with(pageRequest); // Apply pagination

    return reactiveMongoTemplate.find(query, Plantation.class);
  }

  @Override
  public Mono<Long> countAllByFiltersAndPlantationStatusAndIsDeletedIsFalse(String search, String status) {
    Criteria criteria = getCriteriaForFiltersAndHasAlertsAndIsDeletedIsFalse(search, status);

    Query query = new Query(criteria);
    return reactiveMongoTemplate.count(query, Plantation.class);
  }

  @Override
  public Flux<Plantation> findAllByManagersContainingAndFiltersAndPlantationStatusAndIsDeletedIsFalse(String userId, String search, String status, PageRequest pageRequest) {
    Criteria criteria = getCriteriaForManagersContainingAndFiltersAndPlantationStatusAndIsDeletedIsFalse(userId, search, status);

    Query query = new Query(criteria);
    query.with(pageRequest); // Apply pagination

    return reactiveMongoTemplate.find(query, Plantation.class);
  }

  @Override
  public Mono<Long> countByManagersContainingAndFiltersAndPlantationStatusAndIsDeletedIsFalse(String userId, String search, String status) {
    Criteria criteria = getCriteriaForManagersContainingAndFiltersAndPlantationStatusAndIsDeletedIsFalse(userId, search, status);

    Query query = new Query(criteria);
    return reactiveMongoTemplate.count(query, Plantation.class);
  }

  /**
   * This method creates a Criteria object that combines multiple filters
   * @param search search filter of the plantation
   * @return Criteria object that combines multiple filters using an OR operator and is_deleted is false
   */
  private Criteria getCriteriaForFiltersAndIsDeletedIsFalse(String search) {
    return new Criteria().andOperator(
      new Criteria().orOperator(
        Criteria.where("name").regex(search, "i"),
        Criteria.where("country").regex(search, "i"),
        Criteria.where("province").regex(search, "i"),
        Criteria.where("city").regex(search, "i"),
        Criteria.where("type").regex(search, "i")
      ),
      Criteria.where("is_deleted").is(false)
    );
  }

  /**
   * This method creates a Criteria object that combines multiple filters
   * @param userId user id of the possible manager
   * @param search search filter of the plantation
   * @return Criteria object that combines multiple filters using an OR operator and is_deleted is false
   */
  private Criteria getCriteriaForManagersContainingAndFiltersAndIsDeletedIsFalse(String userId, String search) {
    return new Criteria().andOperator(
      new Criteria().orOperator(
        Criteria.where("name").regex(search, "i"),
        Criteria.where("country").regex(search, "i"),
        Criteria.where("province").regex(search, "i"),
        Criteria.where("city").regex(search, "i"),
        Criteria.where("type").regex(search, "i")
      ),
      Criteria.where("is_deleted").is(false),
      Criteria.where("owner_id").is(userId)
    );
  }

  /**
   * This method creates a Criteria object that combines multiple filters
   * @param search search filter of the plantation
   * @param status plantation status of the plantation
   * @return Criteria object that combines multiple filters using an OR operator and is_deleted is false
   */
  private Criteria getCriteriaForFiltersAndHasAlertsAndIsDeletedIsFalse(String search, String status) {
    return new Criteria().andOperator(
      new Criteria().orOperator(
        Criteria.where("name").regex(search, "i"),
        Criteria.where("country").regex(search, "i"),
        Criteria.where("province").regex(search, "i"),
        Criteria.where("city").regex(search, "i"),
        Criteria.where("type").regex(search, "i")
      ),
      Criteria.where("is_deleted").is(false),
      Criteria.where("status").regex(status, "i")
    );
  }

  /**
   * This method creates a Criteria object that combines multiple filters
   * @param userId user id of the possible manager
   * @param search search filter of the plantation
   * @param status plantation status of the plantation
   * @return Criteria object that combines multiple filters using an OR operator and is_deleted is false
   */
  private Criteria getCriteriaForManagersContainingAndFiltersAndPlantationStatusAndIsDeletedIsFalse(String userId, String search, String status) {
    return new Criteria().andOperator(
      new Criteria().orOperator(
        Criteria.where("name").regex(search, "i"),
        Criteria.where("country").regex(search, "i"),
        Criteria.where("province").regex(search, "i"),
        Criteria.where("city").regex(search, "i"),
        Criteria.where("type").regex(search, "i")
      ),
      Criteria.where("is_deleted").is(false),
      Criteria.where("owner_id").is(userId),
      Criteria.where("status").regex(status, "i")
    );
  }
}
