package com.proyecto.apisensores.enums;

public enum SensorUnit {
  CELSIUS,
  PERCENTAGE;

  /**
   * Convert a string to a SensorUnit enum.
   * @param sensorUnitStr string representation of the sensor type
   * @return the corresponding SensorType enum
   */
  public static SensorUnit convertFromString(String sensorUnitStr) {
    try {
      return SensorUnit.valueOf(sensorUnitStr.toUpperCase());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
}
