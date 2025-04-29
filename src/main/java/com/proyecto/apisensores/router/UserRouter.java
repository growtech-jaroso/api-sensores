package com.proyecto.apisensores.router;

import com.proyecto.apisensores.handlers.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class UserRouter {
  private final UserHandler handler;

  public UserRouter(UserHandler handler) {
    this.handler = handler;
  }

  @Bean
  public RouterFunction<ServerResponse> userRoutes() {
    return RouterFunctions.route()
      .GET("/emails", handler::getAllUserEmails) // Route: GET /api/users/emails
      .build();
  }
}
