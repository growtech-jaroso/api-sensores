package com.growtech.api.handlers;

import com.growtech.api.responses.success.DataResponse;
import com.growtech.api.services.sensor_value.SensorValueService;
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
import java.util.List;

@Component
public class SensorValueHandler {

  private final SensorValueService sensorValueService;
  private final ObjectValidator objectValidator;

  public SensorValueHandler(SensorValueService sensorValueService, ObjectValidator objectValidator) {
    this.sensorValueService = sensorValueService;
    this.objectValidator = objectValidator;
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


  public Mono<ServerResponse> getAllSensorValuesByPlantation(ServerRequest serverRequest) {
    Pair<LocalDateTime, LocalDateTime> checkedDates = ParamsUtil.getDateFilter(serverRequest);
    // Recuperate the page request from the request parameters
    String plantationId = serverRequest.pathVariable("plantation_id");

    return AuthUtil.getAuthUser().flatMap(
      user -> this.sensorValueService.getAllSensorValuesByPlantation(plantationId, checkedDates)
        .collectList()
        .flatMap(sensorValues -> ServerResponse.ok()
          .bodyValue(new DataResponse<>(HttpStatus.OK, sensorValues)))
    );

  }

  public Mono<ServerResponse> getAllSensorValuesByTypeByPlantation(ServerRequest serverRequest) {
    Pair<LocalDateTime, LocalDateTime> checkedDates = ParamsUtil.getDateFilter(serverRequest);
    // Recuperate the page request from the request parameters
    String plantationId = serverRequest.pathVariable("plantation_id");
    String sensorType = serverRequest.pathVariable("sensor_type");

    return AuthUtil.getAuthUser().flatMap(
      user -> this.sensorValueService.getAllSensorValuesByTypeByPlantation(plantationId, sensorType, checkedDates)
        .collectList()
        .flatMap(sensorValues -> ServerResponse.ok()
          .bodyValue(new DataResponse<>(HttpStatus.OK, sensorValues)))
    );

  }

  public Mono<ServerResponse> getAllSensorValuesBySensorIdAndPlantationId(ServerRequest serverRequest) {
    Pair<LocalDateTime, LocalDateTime> checkedDates = ParamsUtil.getDateFilter(serverRequest);
    // Recuperate the page request from the request parameters
    String plantationId = serverRequest.pathVariable("plantation_id");
    String sensorId = serverRequest.pathVariable("sensor_id");

    return AuthUtil.getAuthUser().flatMap(
      user -> this.sensorValueService.getAllSensorValuesBySensorIdAndPlantationId(sensorId, plantationId, checkedDates)
        .collectList()
        .flatMap(sensorValues -> ServerResponse.ok()
          .bodyValue(new DataResponse<>(HttpStatus.OK, sensorValues)))
    );
  }

  public Mono<ServerResponse> getAllBySensorIdIn(ServerRequest serverRequest) {
    List<String> sensorIds = serverRequest.queryParam("sensor_ids")
      .map(ids -> List.of(ids.split(",")))
      .orElse(List.of());

    return AuthUtil.getAuthUser().flatMap(
      user -> this.sensorValueService.getAllBySensorIdIn(sensorIds)
        .collectList()
        .flatMap(sensorValues -> ServerResponse.ok()
          .bodyValue(new DataResponse<>(HttpStatus.OK, sensorValues)))
    );
  }

  /**
   * Get all sensor values by sensor id and plantation id
   * @param serverRequest
   * @return
   */
  public Mono<ServerResponse> getAllBySensorIdInAndReadingTimestampBetween(ServerRequest serverRequest) {
    List<String> sensorIds = serverRequest.queryParam("sensor_ids")
      .map(ids -> List.of(ids.split(",")))
      .orElse(List.of());

    LocalDateTime timestampAfter = serverRequest.queryParam("timestamp_after")
      .map(LocalDateTime::parse)
      .orElse(null);

    LocalDateTime timestampBefore = serverRequest.queryParam("timestamp_before")
      .map(LocalDateTime::parse)
      .orElse(null);

    return AuthUtil.getAuthUser().flatMap(
      user -> this.sensorValueService.getAllBySensorIdInAndReadingTimestampBetween(sensorIds, timestampAfter, timestampBefore)
        .collectList()
        .flatMap(sensorValues -> ServerResponse.ok()
          .bodyValue(new DataResponse<>(HttpStatus.OK, sensorValues)))
    );

  }
}
