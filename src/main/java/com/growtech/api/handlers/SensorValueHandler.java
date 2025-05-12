package com.growtech.api.handlers;

import com.growtech.api.enums.SensorType;
import com.growtech.api.exceptions.CustomException;
import com.growtech.api.responses.success.DataResponse;
import com.growtech.api.services.sensor_value.SensorValueService;
import com.growtech.api.utils.AuthUtil;
import com.growtech.api.utils.ParamsUtil;
import com.growtech.api.validators.ObjectValidator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class SensorValueHandler {

  private final SensorValueService sensorValueService;
  private final ObjectValidator objectValidator;
  private final ApplicationEventPublisher applicationEventPublisher;

  public SensorValueHandler(SensorValueService sensorValueService, ObjectValidator objectValidator, ApplicationEventPublisher applicationEventPublisher) {
    this.sensorValueService = sensorValueService;
    this.objectValidator = objectValidator;
    this.applicationEventPublisher = applicationEventPublisher;
  }

  /**
   *
   * @param request
   * @return
   */
  public Mono<ServerResponse> getAllSensorValuesBySensorId(ServerRequest request) {
    Pair<LocalDateTime, LocalDateTime> checkedDates = ParamsUtil.getDateFilter(request);
    // Recuperate the page request from the request parameters
    String sensorId = request.pathVariable("sensor_id");
    String plantationId = request.pathVariable("plantation_id");
    return AuthUtil.getAuthUser().flatMap(
      user -> this.sensorValueService.getAllSensorValuesBySensorId(user, sensorId, plantationId, checkedDates)
        .collectList()
        .flatMap(sensorValues -> ServerResponse.ok()
          .bodyValue(new DataResponse<>(HttpStatus.OK, sensorValues)))
    );
  }

  public Mono<ServerResponse> getAllSensorValuesByTypeByPlantation(ServerRequest serverRequest) {
    Pair<LocalDateTime, LocalDateTime> checkedDates = ParamsUtil.getDateFilter(serverRequest);
    // Recuperate the page request from the request parameters
    String plantationId = serverRequest.pathVariable("plantation_id");
    String sensorTypeStr = serverRequest.pathVariable("sensor_type");

    SensorType sensorType = SensorType.convertFromString(sensorTypeStr);

    if (sensorType == null) {
      throw new CustomException(HttpStatus.BAD_REQUEST, "Sensor type not valid");
    }
    return AuthUtil.getAuthUser().flatMap(
      user -> this.sensorValueService.getAllSensorValuesByTypeByPlantation(user, plantationId, sensorType, checkedDates)
        .collectList()
        .flatMap(sensorValues -> ServerResponse.ok()
          .bodyValue(new DataResponse<>(HttpStatus.OK, sensorValues)))
    );
  }

}
