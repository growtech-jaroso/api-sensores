package com.proyecto.apisensores.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PlantationDto(
  @NotBlank(message = "Name is required")
  @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
  String name,
  @NotNull(message = "Country is required")
  String country,
  @NotNull(message = "Province is required")
  String province,
  @NotNull(message = "City is required")
  String city,
  @NotNull(message = "Description is required")
  String description,
  @NotNull(message = "Type is required")
  String type,
  @JsonProperty("user_email")
  @NotBlank(message = "User email is required")
  @Email(message = "Email must be valid")
  String userEmail
) {
}
