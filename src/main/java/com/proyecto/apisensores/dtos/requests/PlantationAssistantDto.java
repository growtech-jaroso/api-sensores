package com.proyecto.apisensores.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PlantationAssistantDto(
  @NotBlank(message = "Assistant email is required")
  @Email(message = "Assistant email must be valid")
  @JsonProperty("assistant_email")
  String assistantEmail
) {
}
