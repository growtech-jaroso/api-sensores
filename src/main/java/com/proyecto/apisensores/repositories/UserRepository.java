package com.proyecto.apisensores.repositories;

import com.proyecto.apisensores.entities.User;
import com.proyecto.apisensores.enums.UserRole;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
  Mono<User> findByEmail(String email);
  Mono<User> findByEmailAndRolesNotContains(String email, List<UserRole> roles);
  Mono<User> findByUsername(String username);
}
