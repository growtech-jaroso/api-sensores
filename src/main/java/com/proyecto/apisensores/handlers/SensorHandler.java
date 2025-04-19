package com.proyecto.apisensores.handlers;

import com.proyecto.apisensores.entities.Sensor;
import com.proyecto.apisensores.entities.User;
import com.proyecto.apisensores.responses.Response;
import com.proyecto.apisensores.responses.success.paginated.PaginatedResponse;
import com.proyecto.apisensores.services.sensors.SensorService;
import com.proyecto.apisensores.utils.AuthUtil;
import com.proyecto.apisensores.utils.ParamsUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class SensorHandler {

  private final SensorService sensorService;

  public SensorHandler(SensorService sensorService) {
    this.sensorService = sensorService;
  }

  public Mono<ServerResponse> getAllSensorsByPlantation(ServerRequest request) {
    // Create a page request from the request parameters
    PageRequest pageRequest = ParamsUtil.getPageRequest(request);

    // Get the plantation id from the request
    String plantationId = request.pathVariable("plantation_id");

    // Retrieve the user from the request
    Mono<User> authUser = AuthUtil.getAuthUser();

    // Get the sensors by plantation id
    return authUser.
      flatMap(user -> {
        // Get all sensors by plantation id
        // Create a paginated response
        return this.sensorService
            .getSensorsByPlantationPaginated(user, plantationId, pageRequest)
            .flatMap(tuple -> Response.builder(HttpStatus.OK)
                .bodyValue(new PaginatedResponse<>(
                  HttpStatus.OK,
                  tuple.getT2(),
                  pageRequest,
                  tuple.getT1()
                ))
              );
      });
  }
}
