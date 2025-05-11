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
  public Flux<Plantation> findAllByFiltersAndIsDeletedIsFalse(String name, String country, String province, String city, String type, PageRequest pageRequest) {
    Criteria criteria = getCriteriaForFiltersAndIsDeletedIsFalse(name, country, province, city, type);

    Query query = new Query(criteria);
    query.with(pageRequest); // Apply pagination

    return reactiveMongoTemplate.find(query, Plantation.class);
  }

  @Override
  public Mono<Long> countAllByFiltersAndIsDeletedIsFalse(String name, String country, String province, String city, String type) {
    Criteria criteria = getCriteriaForFiltersAndIsDeletedIsFalse(name, country, province, city, type);

    Query query = new Query(criteria);
    return reactiveMongoTemplate.count(query, Plantation.class);
  }

  @Override
  public Flux<Plantation> findAllByManagersContainingAndFiltersAndIsDeletedIsFalse(String userId, String name, String country, String province, String city, String type, PageRequest pageRequest) {
    Criteria criteria = getCriteriaForManagersContainingAndFiltersAndIsDeletedIsFalse(
      userId,
      name,
      country,
      province,
      city,
      type
    );

    Query query = new Query(criteria);
    query.with(pageRequest); // Apply pagination

    return reactiveMongoTemplate.find(query, Plantation.class);
  }

  @Override
  public Mono<Long> countAllByManagersContainingAndFiltersAndIsDeletedIsFalse(String userId, String name, String country, String province, String city, String type) {
    Criteria criteria = getCriteriaForManagersContainingAndFiltersAndIsDeletedIsFalse(
      userId,
      name,
      country,
      province,
      city,
      type
    );

    Query query = new Query(criteria);
    return reactiveMongoTemplate.count(query, Plantation.class);
  }

  @Override
  public Flux<Plantation> findAllByFiltersAndHasAlertsAndIsDeletedIsFalse(String name, String country, String province, String city, String type, Boolean hasAlerts, PageRequest pageRequest) {
    Criteria criteria = getCriteriaForFiltersAndHasAlertsAndIsDeletedIsFalse(
      name,
      country,
      province,
      city,
      type,
      hasAlerts
    );

    Query query = new Query(criteria);
    query.with(pageRequest); // Apply pagination

    return reactiveMongoTemplate.find(query, Plantation.class);
  }

  @Override
  public Mono<Long> countAllByFiltersAndHasAlertsAndIsDeletedIsFalse(String name, String country, String province, String city, String type, Boolean hasAlerts) {
    Criteria criteria = getCriteriaForFiltersAndHasAlertsAndIsDeletedIsFalse(
      name,
      country,
      province,
      city,
      type,
      hasAlerts
    );

    Query query = new Query(criteria);
    return reactiveMongoTemplate.count(query, Plantation.class);
  }

  @Override
  public Flux<Plantation> findAllByManagersContainingAndFiltersAndHasAlertsAndIsDeletedIsFalse(String userId, String name, String country, String province, String city, String type, Boolean hasAlerts, PageRequest pageRequest) {
    Criteria criteria = getCriteriaForManagersContainingAndFiltersAndHasAlertsAndIsDeletedIsFalse(
      userId,
      name,
      country,
      province,
      city,
      type,
      hasAlerts
    );

    Query query = new Query(criteria);
    query.with(pageRequest); // Apply pagination

    return reactiveMongoTemplate.find(query, Plantation.class);
  }

  @Override
  public Mono<Long> countByManagersContainingAndFiltersAndHasAlertsAndIsDeletedIsFalse(String userId, String name, String country, String province, String city, String type, Boolean hasAlerts) {
    Criteria criteria = getCriteriaForManagersContainingAndFiltersAndHasAlertsAndIsDeletedIsFalse(
      userId,
      name,
      country,
      province,
      city,
      type,
      hasAlerts
    );

    Query query = new Query(criteria);
    return reactiveMongoTemplate.count(query, Plantation.class);
  }

  /**
   * This method creates a Criteria object that combines multiple filters
   * @param name name of the plantation
   * @param country country of the plantation
   * @param province province of the plantation
   * @param city city of the plantation
   * @param type type of the plantation
   * @return Criteria object that combines multiple filters using an OR operator and is_deleted is false
   */
  private Criteria getCriteriaForFiltersAndIsDeletedIsFalse(
    String name,
    String country,
    String province,
    String city,
    String type
  ) {
    return new Criteria().andOperator(
      new Criteria().orOperator(
        Criteria.where("name").regex(name, "i"),
        Criteria.where("country").regex(country, "i"),
        Criteria.where("province").regex(province, "i"),
        Criteria.where("city").regex(city, "i"),
        Criteria.where("type").regex(type, "i")
      ),
      Criteria.where("is_deleted").is(false)
    );
  }

  /**
   * This method creates a Criteria object that combines multiple filters
   * @param userId user id of the possible manager
   * @param name name of the plantation
   * @param country country of the plantation
   * @param province province of the plantation
   * @param city city of the plantation
   * @param type type of the plantation
   * @return Criteria object that combines multiple filters using an OR operator and is_deleted is false
   */
  private Criteria getCriteriaForManagersContainingAndFiltersAndIsDeletedIsFalse(
    String userId,
    String name,
    String country,
    String province,
    String city,
    String type
  ) {
    return new Criteria().andOperator(
      new Criteria().orOperator(
        Criteria.where("name").regex(name, "i"),
        Criteria.where("country").regex(country, "i"),
        Criteria.where("province").regex(province, "i"),
        Criteria.where("city").regex(city, "i"),
        Criteria.where("type").regex(type, "i")
      ),
      Criteria.where("is_deleted").is(false),
      Criteria.where("owner_id").is(userId)
    );
  }

  /**
   * This method creates a Criteria object that combines multiple filters
   * @param name name of the plantation
   * @param country country of the plantation
   * @param province province of the plantation
   * @param city city of the plantation
   * @param type type of the plantation
   * @param hasAlerts has alerts of the plantation
   * @return Criteria object that combines multiple filters using an OR operator and is_deleted is false
   */
  private Criteria getCriteriaForFiltersAndHasAlertsAndIsDeletedIsFalse(
    String name,
    String country,
    String province,
    String city,
    String type,
    Boolean hasAlerts
  ) {
    return new Criteria().andOperator(
      new Criteria().orOperator(
        Criteria.where("name").regex(name, "i"),
        Criteria.where("country").regex(country, "i"),
        Criteria.where("province").regex(province, "i"),
        Criteria.where("city").regex(city, "i"),
        Criteria.where("type").regex(type, "i")
      ),
      Criteria.where("is_deleted").is(false),
      Criteria.where("has_alerts").is(hasAlerts)
    );
  }

  /**
   * This method creates a Criteria object that combines multiple filters
   * @param userId user id of the possible manager
   * @param name name of the plantation
   * @param country country of the plantation
   * @param province province of the plantation
   * @param city city of the plantation
   * @param type type of the plantation
   * @param hasAlerts has alerts of the plantation
   * @return Criteria object that combines multiple filters using an OR operator and is_deleted is false
   */
  private Criteria getCriteriaForManagersContainingAndFiltersAndHasAlertsAndIsDeletedIsFalse(
    String userId,
    String name,
    String country,
    String province,
    String city,
    String type,
    Boolean hasAlerts
  ) {
    return new Criteria().andOperator(
      new Criteria().orOperator(
        Criteria.where("name").regex(name, "i"),
        Criteria.where("country").regex(country, "i"),
        Criteria.where("province").regex(province, "i"),
        Criteria.where("city").regex(city, "i"),
        Criteria.where("type").regex(type, "i")
      ),
      Criteria.where("is_deleted").is(false),
      Criteria.where("owner_id").is(userId),
      Criteria.where("has_alerts").is(hasAlerts)
    );
  }
}
