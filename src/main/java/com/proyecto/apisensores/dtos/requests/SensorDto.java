package com.proyecto.apisensores.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.proyecto.apisensores.annotations.IsSensorType;
import com.proyecto.apisensores.annotations.IsSensorUnit;
import com.proyecto.apisensores.enums.SensorUnit;
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
