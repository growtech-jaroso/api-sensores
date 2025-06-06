package com.growtech.api.enums;

public enum DeviceType {
  Sensor,
  Actuator;

  /**
   * Convert a string to a SensorType enum.
   * @param deviceTypeStr string representation of the sensor type
   * @return the corresponding SensorType enum
   */
  public static DeviceType convertFromString(String deviceTypeStr) {
    try {
      return DeviceType.valueOf(deviceTypeStr.toUpperCase());
    } catch (IllegalArgumentException e) {
      // Handle the case where the string does not match any enum constant returning null
      return null;
    }
  }

  public static String getAllSensorDeviceTypes() {
    StringBuilder deviceTypes = new StringBuilder();

    // Iterate through all enum constants and append their names to the StringBuilder
    for (SensorType type : SensorType.values()) {
      deviceTypes.append(type.name().toLowerCase()).append(", ");
    }

    // Remove the last comma and space if there are any sensor types
    if (!deviceTypes.isEmpty()) {
      deviceTypes.setLength(deviceTypes.length() - 2);
    }

    return deviceTypes.toString();
  }
}
