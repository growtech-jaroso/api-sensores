package com.growtech.api.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EditPlantationDto(
  @NotBlank(message = "Name is required")
  @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
  String name,
  @NotBlank(message = "Plantation type is required")
  String type
) {
}
