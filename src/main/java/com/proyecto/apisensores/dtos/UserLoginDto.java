package com.proyecto.apisensores.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserLoginDto(
  @NotBlank(message = "Email is required")
  @Email(message = "Email is not valid")
  String email,
  @NotBlank(message = "Password is required")
  String password
){}
