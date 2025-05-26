package com.growtech.api.enums;

public enum PlantationStatus {
  ONLINE,
  OFFLINE;

  /**
   * Convert a string to a SensorStatus enum.
   * @param statusStr string representation of the sensor status
   * @return the corresponding SensorStatus enum
   */
  public static PlantationStatus convertFromString(String statusStr) {
      try {
          return PlantationStatus.valueOf(statusStr.toUpperCase());
      } catch (IllegalArgumentException e) {
          // Handle the case where the string does not match any enum constant returning null
          return null;
      }
  }
}
