package com.growtech.api.dtos.responses;

import com.growtech.api.enums.UserRole;

public record UserInfo(
  String id,
  String username,
  String email,
  UserRole role
) {
}
