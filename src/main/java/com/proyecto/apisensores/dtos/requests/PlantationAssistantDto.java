package com.proyecto.apisensores.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PlantationAssistantDto(
  @NotBlank(message = "Manager email is required")
  @Email(message = "Manager email must be valid")
  @JsonProperty("manager_email")
  String managerEmail
) {
}
