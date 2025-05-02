package com.growtech.api.enums;

public enum UserRole {
  USER, // Default role for all users
  SUPPORT, // Support role for users that can check all but not modify, create or delete data
  ADMIN // Admin role for users that can do everything
}
