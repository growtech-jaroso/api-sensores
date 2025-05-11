package com.growtech.api.repositories.user;

import com.growtech.api.entities.User;
import com.growtech.api.enums.UserRole;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

  private final ReactiveMongoTemplate reactiveMongoTemplate;

  public UserRepositoryCustomImpl(ReactiveMongoTemplate reactiveMongoTemplate) {
    this.reactiveMongoTemplate = reactiveMongoTemplate;
  }

  @Override
  public Mono<Long> countByUsernameOrEmailAndRole(String username, String email, UserRole role) {
    Criteria criteria = getCriteriaForUsernameOrEmailAndRole(username, email, role);

    // Create query with the criteria
    Query query = new Query(criteria);
    // Execute the query and return the count
    return reactiveMongoTemplate.count(query, User.class);
  }

  @Override
  public Flux<User> findMatchingUsers(String username, String email, UserRole role, PageRequest pageRequest) {

    Criteria criteria = getCriteriaForUsernameOrEmailAndRole(username, email, role);

    // Create query with the criteria
    Query query = new Query(criteria);
    query.with(pageRequest);  // Apply pagination

    // Execute the query and return the users
    return reactiveMongoTemplate.find(query, User.class);
  }

  /**
   * Creates a criteria for finding users by username or email and role.
   * @param username username to search for
   * @param email email to search for
   * @param role role to search for
   * @return Criteria object for the query
   */
  private Criteria getCriteriaForUsernameOrEmailAndRole(String username, String email, UserRole role) {
    return new Criteria().andOperator(
      new Criteria().orOperator(
        Criteria.where("username").regex(username, "i"),
        Criteria.where("email").regex(email, "i")
      ),
      Criteria.where("role").is(role)
    );
  }
}
