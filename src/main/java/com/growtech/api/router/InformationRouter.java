package com.growtech.api.router;

import com.growtech.api.handlers.InformationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class InformationRouter {

  private final InformationHandler handler;

  public InformationRouter(InformationHandler handler) {
    this.handler = handler;
  }

  @Bean
  public RouterFunction<ServerResponse> informationRoutes() {
    return RouterFunctions.route()
      .GET("/user_roles", handler::getAllUserRoles) // Route: GET /api/information/user_roles
      .GET("/sensor_types", handler::getAllSensorTypes) // Route: GET /api/information/sensor_types
      .GET("/sensor_units", handler::getAllSensorUnits) // Route: GET /api/information/sensor_units
      .build();
  }
}
