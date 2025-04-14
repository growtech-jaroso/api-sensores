package com.proyecto.apisensores.dtos;

import jakarta.validation.constraints.NotNull;

public record UserRegisterDto(
  @NotNull
  String username,
  @NotNull
  String email,
  @NotNull
  String password) {
}
