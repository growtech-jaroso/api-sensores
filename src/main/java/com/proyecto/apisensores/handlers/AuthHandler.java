package com.proyecto.apisensores.handlers;

import com.proyecto.apisensores.services.users.UserService;
import com.proyecto.apisensores.utils.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class AuthHandler {

  private final UserService userService;

  public AuthHandler(UserService userService) {
    this.userService = userService;
  }

  public Mono<ServerResponse> register(ServerRequest request) {
    return this.userService.register(request);
  }

  public Mono<ServerResponse> login(ServerRequest request) {
    return this.userService.login(request);
  }

}
