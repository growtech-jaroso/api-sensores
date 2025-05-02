package com.growtech.api.router;

import com.growtech.api.handlers.AuthHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class AuthRouter {
  private final AuthHandler handler;

  public AuthRouter(AuthHandler handler) {
    this.handler = handler;
  }

  @Bean
  public RouterFunction<ServerResponse> authRoutes() {
    return RouterFunctions.route()
      .POST("login", handler::login) // Route: POST /api/auth/login
      .POST("register", handler::register) // Route: POST /api/auth/register
      .build();
  }
}
