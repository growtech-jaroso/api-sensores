package com.growtech.api.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.growtech.api.annotations.IsSensorType;
import com.growtech.api.annotations.IsSensorUnit;
import jakarta.validation.constraints.NotBlank;

public record SensorDto(
  @JsonProperty("sensor_type")
  @NotBlank(message = "Sensor type is required")
  @IsSensorType
  String sensorType,
  @JsonProperty("sensor_unit")
  @NotBlank(message = "Sensor unit is required")
  @IsSensorUnit
  String sensorUnit
) {}
