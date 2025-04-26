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

  public static String getAllSensorTypes() {
      StringBuilder sensorTypes = new StringBuilder();

      // Iterate through all enum constants and append their names to the StringBuilder
      for (SensorType type : SensorType.values()) {
          sensorTypes.append(type.name().toLowerCase()).append(", ");
      }

      // Remove the last comma and space if there are any sensor types
      if (!sensorTypes.isEmpty()) {
          sensorTypes.setLength(sensorTypes.length() - 2);
      }

      return sensorTypes.toString();
  }
}
