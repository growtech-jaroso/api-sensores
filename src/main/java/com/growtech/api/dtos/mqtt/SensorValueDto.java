package com.growtech.api.dtos.mqtt;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.growtech.api.entities.SensorValue;
import com.growtech.api.enums.MeasureTimespan;
import jakarta.validation.constraints.NotBlank;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public record SensorValueDto(
  @NotBlank
  Double measurement,
  @NotBlank
  @JsonProperty("reading_timestamp")
  String readingTimestamp,
  @NotBlank
  @JsonProperty("measure_timespan")
  String measureTimespan
) {
  public SensorValue toSensorValue(String sensorId) {
    MeasureTimespan measureTimespan = MeasureTimespan.convertFromString(this.measureTimespan);
    if (measureTimespan == null) {
      throw new IllegalArgumentException("Invalid measure timespan: " + this.measureTimespan);
    }

    LocalDateTime readingTimestamp;

    try {
      readingTimestamp = LocalDateTime.parse(this.readingTimestamp);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid reading timestamp: " + this.readingTimestamp, e);
    }

    return new SensorValue(this.measurement, readingTimestamp, measureTimespan, sensorId);
  }
}
