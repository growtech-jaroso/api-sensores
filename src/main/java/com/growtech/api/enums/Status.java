package com.growtech.api.enums;

public enum Status {
  ON,
  OFF;


  /**
   * Convert a string to a SensorType enum.
   * @param statusStr string representation of the sensor type
   * @return the corresponding SensorType enum
   */
  public static Status convertFromString(String statusStr) {
    try {
      return Status.valueOf(statusStr.toUpperCase());
    } catch (IllegalArgumentException e) {
      // Handle the case where the string does not match any enum constant returning null
      return null;
    }
  }
}
