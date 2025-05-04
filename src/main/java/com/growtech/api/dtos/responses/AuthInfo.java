package com.growtech.api.dtos.responses;

import com.growtech.api.enums.UserRole;

public record AuthInfo(
  String token,
  String username,
  String email,
  UserRole role
) { }
