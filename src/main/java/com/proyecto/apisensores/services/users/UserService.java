package com.proyecto.apisensores.services.users;

import com.proyecto.apisensores.entities.User;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public interface UserService {
  Mono<ServerResponse> register(ServerRequest request);
  Mono<ServerResponse> login(ServerRequest request);
  Mono<User> findByUsername(String username);
  Mono<User> findByEmail(String email);
}
