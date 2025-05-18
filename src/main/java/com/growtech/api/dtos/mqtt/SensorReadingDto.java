package com.growtech.api.dtos.mqtt;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.growtech.api.entities.SensorValue;
import com.growtech.api.enums.MeasureTimespan;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Slf4j
public record SensorReadingDto(
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
      log.error("Invalid measure timespan: {}", this.measureTimespan);
      return null;
    }

    // Parse the reading timestamp using the formatter
    LocalDateTime readingTimestamp;

    try {
      Instant instant = Instant.parse(this.readingTimestamp);
      readingTimestamp = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);

    } catch (Exception e) {
      log.error("Invalid reading timestamp: {}", this.readingTimestamp);
      return null;
    }

    Double roundedMeasurement = new BigDecimal(this.measurement)
      .setScale(2, RoundingMode.HALF_UP)
      .doubleValue();

    return new SensorValue(roundedMeasurement, readingTimestamp, measureTimespan, sensorId);
  }
}
