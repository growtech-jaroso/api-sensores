package com.proyecto.apisensores.dtos;

import com.proyecto.apisensores.enums.UserRole;

import java.util.List;

public record AuthInfo(
  String token,
  String username,
  String email,
  List<UserRole> roles
) { }
