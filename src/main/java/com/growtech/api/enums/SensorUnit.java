package com.growtech.api.enums;

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

  public static String getAllSensorUnits() {
    StringBuilder sensorTypes = new StringBuilder();

    // Iterate through all enum constants and append their names to the StringBuilder
    for (SensorUnit unit : SensorUnit.values()) {
      sensorTypes.append(unit.name().toLowerCase()).append(", ");
    }

    // Remove the last comma and space if there are any sensor types
    if (!sensorTypes.isEmpty()) {
      sensorTypes.setLength(sensorTypes.length() - 2);
    }

    return sensorTypes.toString();
  }
}
