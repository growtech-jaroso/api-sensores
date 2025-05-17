package com.growtech.api.enums;

public enum MeasureTimespan {
  AVG_LAST_1_MINUTE;

  /**
   * Convert a string to a SensorType enum.
   * @param measureTimespanStr string representation of the sensor type
   * @return the corresponding SensorType enum
   */
  public static MeasureTimespan convertFromString(String measureTimespanStr) {
    try {
      return MeasureTimespan.valueOf(measureTimespanStr.toUpperCase());
    } catch (IllegalArgumentException e) {
      // Handle the case where the string does not match any enum constant returning null
      return null;
    }
  }
}
