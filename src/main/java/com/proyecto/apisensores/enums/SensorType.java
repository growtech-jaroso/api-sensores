package com.proyecto.apisensores.enums;

public enum SensorType {
  TEMPERATURE,
  HUMIDITY,
  PRESSURE;

  /**
   * Convert a string to a SensorType enum.
   * @param sensorTypeStr string representation of the sensor type
   * @return the corresponding SensorType enum
   */
  public static SensorType convertFromString(String sensorTypeStr) {
      try {
          return SensorType.valueOf(sensorTypeStr.toUpperCase());
      } catch (IllegalArgumentException e) {
          // Handle the case where the string does not match any enum constant returning null
          return null;
      }
  }
}
