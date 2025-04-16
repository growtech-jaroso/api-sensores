package com.proyecto.apisensores.dtos;

public record AuthInfo(
  String token,
  String username,
  String email
) { }
