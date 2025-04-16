package com.proyecto.apisensores.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserLoginDto(
  @NotBlank(message = "Username is required")
  @Min(value = 3, message = "Username must be at least 3 characters")
  String username,
  @NotBlank(message = "Password is required")
  @Min(value = 8, message = "Password must be at least 8 characters")
  String password
){}
