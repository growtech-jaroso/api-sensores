package com.growtech.api.dtos;

import com.growtech.api.enums.UserRole;

import java.util.List;

public record AuthInfo(
  String token,
  String username,
  String email,
  UserRole role
) { }
