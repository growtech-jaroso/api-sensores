package com.growtech.api.enums;

import com.growtech.api.dtos.mqtt.SensorStatusDto;

public enum Status {
  ON,
  OFF;


  /**
   * Convert a string to a SensorType enum.
   *
   * @param statusStr string representation of the sensor type
   * @return the corresponding SensorType enum
   */
  public static SensorStatusDto convertFromString(String statusStr) {
    try {
      return Status.valueOf(statusStr.toUpperCase());
    } catch (IllegalArgumentException e) {
      // Handle the case where the string does not match any enum constant returning null
      return null;
    }
  }
}
