package com.growtech.api.router;

import com.growtech.api.handlers.UserHandler;
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
      .GET("", handler::getAllUsers) // Route: GET /api/users
      .GET("/{user_id}", handler::getUserById) // Route: GET /api/users/{user_id}
      .DELETE("/{user_id}", handler::deleteUser) // Route: DELETE /api/users/{user_id}
      .GET("/emails", handler::getAllUserEmails) // Route: GET /api/users/emails
      .build();
  }
}
