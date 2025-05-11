package com.growtech.api.repositories.user;

import com.growtech.api.entities.User;
import com.growtech.api.enums.UserRole;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepositoryCustom {
  Mono<Long> countByUsernameOrEmailAndRole(String search, UserRole role);
  Flux<User> findMatchingUsers(String search, UserRole role, PageRequest pageRequest);
}
