package com.growtech.api.repositories;

import com.growtech.api.entities.User;
import com.growtech.api.enums.UserRole;
import com.growtech.api.services.users.EmailProjection;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
  Mono<User> findByEmail(String email);
  Mono<User> findByEmailAndRolesNotContains(String email, List<UserRole> roles);
  Mono<User> findByUsername(String username);
  Flux<User> findAllBy(PageRequest pageRequest);
  Flux<EmailProjection> findAllByRolesNotContains(List<UserRole> roles);
  Mono<User> findUserById(String id);
}
