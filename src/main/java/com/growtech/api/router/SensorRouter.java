package com.growtech.api.router;

import com.growtech.api.handlers.SensorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class SensorRouter {
  private final SensorHandler handler;

  public SensorRouter(SensorHandler handler) {
    this.handler = handler;
  }

  @Bean
  public RouterFunction<ServerResponse> sensorRoutes() {
    return RouterFunctions.route()
      .GET("", handler::getAllSensorsByPlantation) // Route: POST /api/plantations/{plantation_id}/sensors
      .POST("", handler::createSensor) // Route: POST /api/plantations/{plantation_id}/sensors
      .build();
  }
}
