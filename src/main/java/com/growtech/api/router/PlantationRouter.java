package com.growtech.api.router;

import com.growtech.api.handlers.PlantationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Locale;

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
      .GET("/owners", this.handler::getAllPlantationsOwners) // Route: GET /api/plantations/owners
      .GET("/owners/{user_id}", this.handler::getPlantationsByUserId) // Route: GET /api/plantations/owners/{user_id}
      .GET("/{plantation_id}", this.handler::getPlantationById) // Route: GET /api/plantations/{plantation_id}
      .PUT("/{plantation_id}", this.handler::editPlantation) // Route: PUT /api/plantations/{plantation_id}
      .POST("" , this.handler::createPlantation) // Route: POST /api/plantations
      .DELETE("/{plantation_id}", this.handler::deletePlantation) // Route: DELETE /api/plantations/{plantation_id}
      .POST("/{plantation_id}/assistants", this.handler::addPlantationManager) // Route: POST /api/plantations/{plantation_id}/assistants
      .DELETE("/{plantation_id}/assistants", this.handler::deletePlantationManager) // Route: DELETE /api/plantations/{plantation_id}/assistants
      .build();
  }
}
