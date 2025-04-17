package com.proyecto.apisensores.router;

import com.proyecto.apisensores.handlers.PlantationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class PlantationRouter {

  private final PlantationHandler handler;

  public PlantationRouter(PlantationHandler handler) {
    this.handler = handler;
  }

  @Bean
  public RouterFunction<ServerResponse> plantationRoutes() {
    return RouterFunctions.route()
      .GET("", handler::getAllPlantationsByUser) // Route: GET /api/plantations
      .POST("" , handler::createPlantation) // Route: POST /api/plantations
      .build();
  }
}
