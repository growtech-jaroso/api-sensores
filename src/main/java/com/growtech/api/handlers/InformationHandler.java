package com.growtech.api.handlers;

import com.growtech.api.enums.UserRole;
import com.growtech.api.responses.Response;
import com.growtech.api.responses.success.DataResponse;
import com.growtech.api.services.information.InformationService;
import com.growtech.api.utils.AuthUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class InformationHandler {

  private final InformationService informationService;

  public InformationHandler(InformationService informationService) {
    this.informationService = informationService;
  }

  public Mono<ServerResponse> getAllUserRoles(ServerRequest request) {
    // Check if user is admin and get all user roles
    return AuthUtil.checkIfUserHaveRoles(UserRole.ADMIN)
      .then(informationService.getAllUserRoles())
      .flatMap(userRoles -> Response
        .builder(HttpStatus.OK)
        .bodyValue(new DataResponse<>(HttpStatus.OK, userRoles))
      );
  }

  public Mono<ServerResponse> getAllSensorTypes(ServerRequest request) {
    // Check if user is admin and get all sensor types
    return AuthUtil.checkIfUserHaveRoles(UserRole.ADMIN)
      .then(informationService.getAllSensorTypes())
      .flatMap(sensorTypes -> Response
        .builder(HttpStatus.OK)
        .bodyValue(new DataResponse<>(HttpStatus.OK, sensorTypes))
      );
  }

  public Mono<ServerResponse> getAllSensorUnits(ServerRequest request) {
    // Check if user is admin and get all sensor units
    return AuthUtil.checkIfUserHaveRoles(UserRole.ADMIN)
      .then(informationService.getAllSensorUnits())
      .flatMap(sensorUnits -> Response
        .builder(HttpStatus.OK)
        .bodyValue(new DataResponse<>(HttpStatus.OK, sensorUnits))
      );
  }

  public Mono<ServerResponse> getAllDeviceSensor(ServerRequest serverRequest) {
    // Check if user is admin and get all sensor units
    return AuthUtil.checkIfUserHaveRoles(UserRole.ADMIN)
      .then(informationService.getAllDeviceSensor())
      .flatMap(deviceSensor -> Response
        .builder(HttpStatus.OK)
        .bodyValue(new DataResponse<>(HttpStatus.OK, deviceSensor))
      );
  }
}
