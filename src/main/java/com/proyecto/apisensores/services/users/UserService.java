package com.proyecto.apisensores.services.users;

import com.proyecto.apisensores.dtos.UserRegisterDto;
import com.proyecto.apisensores.entities.User;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public interface UserService {
  Mono<User> findByUsername(String username);
  Mono<User> findByEmail(String email);
  Boolean passwordMatches(String rawPassword, String encodedPassword);
  Mono<User> save(UserRegisterDto userRegisterDTO);
}
