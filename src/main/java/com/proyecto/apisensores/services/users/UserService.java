package com.proyecto.apisensores.services.users;

import com.proyecto.apisensores.dtos.UserRegisterDto;
import com.proyecto.apisensores.entities.User;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public interface UserService {
  Mono<User> findByEmail(String email);
  Mono<User> findByUsernameOrEmail(String username, String email);
  Mono<User> findByUsername(String username);
  Boolean passwordMatches(String rawPassword, String encodedPassword);
  Mono<User> save(UserRegisterDto userRegisterDTO);
  Mono<Boolean> existsByEmail(String email);
  Mono<Boolean> existsByUsername(String username);
}
