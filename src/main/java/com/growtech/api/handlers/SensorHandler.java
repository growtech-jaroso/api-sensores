package com.growtech.api.handlers;

import com.growtech.api.dtos.mqtt.SensorStatusDto;
import com.growtech.api.dtos.requests.SensorDto;
import com.growtech.api.entities.User;
import com.growtech.api.enums.ActuatorStatus;
import com.growtech.api.enums.UserRole;
import com.growtech.api.exceptions.EmptyBody;
import com.growtech.api.responses.Response;
import com.growtech.api.responses.success.DataResponse;
import com.growtech.api.responses.success.SuccessResponse;
import com.growtech.api.responses.success.paginated.PaginatedResponse;
import com.growtech.api.services.sensors.SensorService;
import com.growtech.api.utils.AuthUtil;
import com.growtech.api.utils.ParamsUtil;
import com.growtech.api.validators.ObjectValidator;
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
    PageRequest pageRequest = ParamsUtil.getPageRequest(request, 1000);

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
      .then(
        request.bodyToMono(SensorDto.class)
          .doOnNext(objectValidator::validate)
          .switchIfEmpty(Mono.error(new EmptyBody()))
      );

    // Get the plantation id from the request
    String plantationId = request.pathVariable("plantation_id");

    // Create a new sensor and return in response
    return sensorDto
      .flatMap(sensorInfo -> this.sensorService.createSensor(sensorInfo, plantationId))
      .flatMap(sensor -> Response
        .builder(HttpStatus.CREATED)
        .bodyValue(new DataResponse<>(HttpStatus.CREATED, sensor))
      );
  }

  public Mono<ServerResponse> createActuatorSensor(ServerRequest request) {
    // Get the plantation id from the request
    String plantationId = request.pathVariable("plantation_id");

    return AuthUtil.checkIfUserHaveRoles(UserRole.ADMIN)
      // Validate the request body
      .then(
        this.sensorService.createActuatorSensor(plantationId)
      .flatMap(sensor -> Response
        .builder(HttpStatus.CREATED)
        .bodyValue(new DataResponse<>(HttpStatus.CREATED, sensor))
      ));
  }

  public Mono<ServerResponse> updateActuatorSensor(ServerRequest request) {
    String plantationId = request.pathVariable("plantation_id");
    String sensorId = request.pathVariable("sensor_id");

    // Get the sensor status from the request body and throw an error if body is empty
    Mono<SensorStatusDto> sensorStatusDto = request.bodyToMono(SensorStatusDto.class)
      .switchIfEmpty(Mono.error(new EmptyBody()));

    // Validate the sensor status
    var validatedSensorStatus = sensorStatusDto.doOnNext(objectValidator::validate);

    return Mono.zip(validatedSensorStatus, AuthUtil.getAuthUser())
      .flatMap(tuple -> this.sensorService.updateActuatorSensor(tuple.getT2(), sensorId, plantationId, ActuatorStatus.convertFromString(tuple.getT1().status())))
      .flatMap(sensor -> Response
        .builder(HttpStatus.OK)
        .bodyValue(new DataResponse<>(HttpStatus.OK, sensor))
      );
  }


  public Mono<ServerResponse> deleteSensor(ServerRequest request) {
    // Get the plantation id from the request
    String plantationId = request.pathVariable("plantation_id");
    // Get the sensor id from the request
    String sensorId = request.pathVariable("sensor_id");

    // Check if user is admin to delete the sensor
    return AuthUtil.checkIfUserHaveRoles(UserRole.ADMIN)
      .then(this.sensorService.deleteSensor(sensorId, plantationId)
        .flatMap(message -> Response
          .builder(HttpStatus.OK)
          .bodyValue(new SuccessResponse(HttpStatus.OK, message))
        ));
  }
}
