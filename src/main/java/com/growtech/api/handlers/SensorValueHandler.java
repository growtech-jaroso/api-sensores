package com.growtech.api.handlers;

import com.growtech.api.responses.success.DataResponse;
import com.growtech.api.services.sensorValue.SensorValueService;
import com.growtech.api.utils.AuthUtil;
import com.growtech.api.utils.ParamsUtil;
import com.growtech.api.validators.ObjectValidator;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
public class SensorValueHandler {

  private final SensorValueService sensorValueService;
  private final ObjectValidator objectValidator;

  public SensorValueHandler(SensorValueService sensorValueService, ObjectValidator objectValidator) {
    this.sensorValueService = sensorValueService;
    this.objectValidator = objectValidator;
  }

  public Mono<ServerResponse> getAllSensorValuesBySensorId(ServerRequest request) {
    Pair<LocalDateTime, LocalDateTime> checkedDates = ParamsUtil.getDateFilter(request);
    // Get the page request from the request parameters
    String sensorId = request.pathVariable("sensor_id");
    String plantationId = request.pathVariable("plantation_id");
    return AuthUtil.getAuthUser().flatMap(
      user -> this.sensorValueService.getAllSensorValuesBySensorId(user, sensorId, plantationId, checkedDates)
        .collectList()
        .flatMap(sensorValues -> ServerResponse.ok()
          .bodyValue(new DataResponse<>(HttpStatus.OK, sensorValues)))
    );
  }

}
