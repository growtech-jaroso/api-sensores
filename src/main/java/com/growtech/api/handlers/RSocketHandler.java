package com.growtech.api.handlers;

import com.growtech.api.dtos.rsocket.SensorSubscriptionRequest;
import com.growtech.api.entities.Sensor;
import com.growtech.api.entities.SensorValue;
import com.growtech.api.exceptions.CustomException;
import com.growtech.api.services.rsocket.RSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Slf4j
@Component
public class RSocketHandler {

  private final RSocketService rsocketService;
  private final Sinks.Many<SensorValue> sink;

  public RSocketHandler(@Qualifier("rsocketServiceImpl") RSocketService rsocketService) {
    this.sink = Sinks.many().multicast().onBackpressureBuffer();
    this.rsocketService = rsocketService;
  }


  @MessageMapping("sensor.values")
  public Flux<SensorValue> streamSensorUpdates(SensorSubscriptionRequest request) {

    log.info("Streaming sensor updates for sensorId: {}", request.sensorId());

    if (request.sensorId() == null || request.plantationId() == null) {
      return Flux.error(new CustomException(HttpStatus.BAD_REQUEST, "sensorId and plantationId must not be null"));
    }

    Mono<Sensor> sensor = this.rsocketService.getSensorId(request.sensorId())
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Sensor not found")));

    return sensor.flatMapMany(s -> sink.asFlux()
      .filter(value ->
        value.getSensorId().equals(request.sensorId()) &&
          s.getPlantationId().equals(request.plantationId())
      ));
  }

  public void update(SensorValue sensorValue) {
    sink.tryEmitNext(sensorValue);
  }
}
