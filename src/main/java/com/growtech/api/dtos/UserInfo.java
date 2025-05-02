package com.growtech.api.dtos;

import com.growtech.api.enums.UserRole;

import java.util.List;

public record UserInfo(
  String id,
  String username,
  String email,
  List<UserRole> roles
) {
}
