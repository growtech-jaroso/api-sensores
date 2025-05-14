package com.growtech.api.dtos.mqtt;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.growtech.api.enums.SensorStatus;

public record SensorStatusDto(
  SensorStatus status,
  @JsonProperty("message_timestamp")
  String messageTimestamp
) {
}
