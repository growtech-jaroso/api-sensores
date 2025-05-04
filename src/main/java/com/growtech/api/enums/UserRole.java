package com.growtech.api.enums;

public enum UserRole {
  USER, // Default role for all users
  SUPPORT, // Support role for users that can check all but not modify, create or delete data
  ADMIN; // Admin role for users that can do everything

  public static UserRole convertFromString(String userRoleStr) {
    try {
      return UserRole.valueOf(userRoleStr.toUpperCase());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  public static String getAllUserRoles() {
    StringBuilder userRoles = new StringBuilder();

    // Iterate through all enum constants and append their names to the StringBuilder
    for (UserRole role : UserRole.values()) {
      userRoles.append(role.name().toLowerCase()).append(", ");
    }

    // Remove the last comma and space if there are any sensor types
    if (!userRoles.isEmpty()) {
      userRoles.setLength(userRoles.length() - 2);
    }

    return userRoles.toString();
  }
}
