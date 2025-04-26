package com.proyecto.apisensores.handlers;

import com.proyecto.apisensores.dtos.requests.SensorDto;
import com.proyecto.apisensores.entities.User;
import com.proyecto.apisensores.enums.UserRole;
import com.proyecto.apisensores.responses.Response;
import com.proyecto.apisensores.responses.success.DataResponse;
import com.proyecto.apisensores.responses.success.paginated.PaginatedResponse;
import com.proyecto.apisensores.services.sensors.SensorService;
import com.proyecto.apisensores.utils.AuthUtil;
import com.proyecto.apisensores.utils.ParamsUtil;
import com.proyecto.apisensores.validators.ObjectValidator;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class SensorHandler {

  private final SensorService sensorService;
  private final ObjectValidator objectValidator;

  public SensorHandler(SensorService sensorService, ObjectValidator objectValidator) {
    this.sensorService = sensorService;
    this.objectValidator = objectValidator;
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

  public Mono<ServerResponse> createSensor(ServerRequest request) {
    Mono<SensorDto> sensorDto = AuthUtil.checkIfUserHaveRoles(UserRole.ADMIN)
      // Validate the request body
      .then(request.bodyToMono(SensorDto.class).doOnNext(objectValidator::validate));

    String plantationId = request.pathVariable("plantation_id");

    // Create a new sensor and return in response
    return sensorDto
      .flatMap(sensorInfo -> this.sensorService.createSensor(sensorInfo, plantationId))
      .flatMap(sensor -> Response
        .builder(HttpStatus.CREATED)
        .bodyValue(new DataResponse<>(HttpStatus.CREATED, sensor))
      );
  }
}
