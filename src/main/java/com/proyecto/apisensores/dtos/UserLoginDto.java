package com.proyecto.apisensores.dtos;

import jakarta.validation.constraints.NotNull;

public record UserLoginDto(
  @NotNull
  String username,
  @NotNull
  String password
){}
