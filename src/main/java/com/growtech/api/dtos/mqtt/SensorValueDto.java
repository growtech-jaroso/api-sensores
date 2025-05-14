package com.growtech.api.dtos.mqtt;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SensorValueDto(
  Double value,
  @JsonProperty("reading_timestamp")
  String readingTimestamp,
  @JsonProperty("measure_timespan")
  String measureTimespan
) {
}
