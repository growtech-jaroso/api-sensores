package com.growtech.api.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CoordinateDto (
  @NotBlank(message = "Latitude is required")
  @Pattern(regexp = "^(-?\\d{1,3}(\\.\\d+)?)$", message = "Invalid latitude of the coordinate")
  String latitude,
  @NotBlank(message = "Longitude is required")
  @Pattern(regexp = "^(-?\\d{1,3}(\\.\\d+)?)$", message = "Invalid longitude of the coordinate")
  String longitude
) {}
