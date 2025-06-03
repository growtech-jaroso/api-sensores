package com.growtech.api.dtos.rsocket;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SensorSubscriptionRequest (
  @JsonProperty("plantation_id")
  String plantationId,
  @JsonProperty("sensor_id")
  String sensorId
){

}
