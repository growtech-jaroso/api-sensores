package com.growtech.api.dtos.mqtt;

import jakarta.validation.constraints.NotBlank;

public record SensorStatusDto(
  @NotBlank
  String status
) { }
