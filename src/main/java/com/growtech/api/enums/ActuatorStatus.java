package com.growtech.api.enums;

public enum ActuatorStatus {
  ON,
  OFF;


  /**
   * Convert a string to a ActuatorStatus enum.
   *
   * @param actuatorStatusStr string representation of the actuator status
   * @return the corresponding ActuatorStatus enum
   */
  public static ActuatorStatus convertFromString(String actuatorStatusStr) {
    try {
      return ActuatorStatus.valueOf(actuatorStatusStr.toUpperCase());
    } catch (IllegalArgumentException e) {
      // Handle the case where the string does not match any enum constant returning null
      return null;
    }
  }
}
