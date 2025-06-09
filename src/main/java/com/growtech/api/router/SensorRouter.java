package com.growtech.api.router;

import com.growtech.api.handlers.SensorHandler;
import com.growtech.api.handlers.SensorValueHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class SensorRouter {
  private final SensorHandler handler;
  private final SensorValueHandler sensorValueHandler;

  public SensorRouter(SensorHandler handler, SensorValueHandler sensorValueHandler) {
    this.handler = handler;
    this.sensorValueHandler = sensorValueHandler;
  }

  @Bean
  public RouterFunction<ServerResponse> sensorRoutes() {
    return RouterFunctions.route()
      .GET("", handler::getAllSensorsByPlantation) // Route: POST /api/plantations/{plantation_id}/sensors
      .POST("", handler::createSensor) // Route: POST /api/plantations/{plantation_id}/sensors
      .POST("/actuator", handler::createActuatorSensor) // Route: POST /api/plantations/{plantation_id}/sensors/actuator
      .PUT("/{sensor_id}/actuator/update", handler::updateActuatorSensor) // Route: POST /api/plantations/{plantation_id}/sensors/{sensor_id}/actuator/update
      .DELETE("/{sensor_id}", handler::deleteSensor) // Route: DELETE /api/plantations/{plantation_id}/sensors/{sensor_id}
      .GET("/{sensor_id}/values", sensorValueHandler::getAllSensorValuesBySensorId) // Route: GET /api/plantations/{plantation_id}/sensors/{sensor_id}/values
      .GET("/type/{sensor_type}/values", sensorValueHandler::getAllSensorValuesByTypeByPlantation) // Route: GET /api/plantations/{plantation_id}/sensors/{sensor_type}/values
      .build();
  }
}
