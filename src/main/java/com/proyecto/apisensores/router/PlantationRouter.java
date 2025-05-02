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
  private final SensorRouter sensorRouter;

  public PlantationRouter(PlantationHandler handler, SensorRouter sensorRouter) {
    this.handler = handler;
    this.sensorRouter = sensorRouter;
  }

  @Bean
  public RouterFunction<ServerResponse> plantationRoutes() {
    return RouterFunctions.route()
      .path("/{plantation_id}/sensors", this.sensorRouter::sensorRoutes) // Sensors routes
      .GET("", this.handler::getAllPlantationsByUser) // Route: GET /api/plantations
      .POST("" , this.handler::createPlantation) // Route: POST /api/plantations
      .POST("/{plantation_id}/assistants", this.handler::addPlantationsAssistants) // Route: POST /api/plantations/{plantation_id}/assistants
      .build();
  }
}
