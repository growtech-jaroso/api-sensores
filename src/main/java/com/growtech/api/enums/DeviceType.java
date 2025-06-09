package com.growtech.api.enums;

public enum DeviceType {
  SENSOR,
  ACTUATOR;

  /**
   * Convert a string to a DeviceType enum.
   * @param deviceTypeStr string representation of device type
   * @return the corresponding DeviceType enum∫
   */
  public static DeviceType convertFromString(String deviceTypeStr) {
    try {
      return DeviceType.valueOf(deviceTypeStr.toUpperCase());
    } catch (IllegalArgumentException e) {
      // Handle the case where the string does not match any enum constant returning null
      return null;
    }
  }

  public static String getAllDeviceTypes() {
    StringBuilder deviceTypes = new StringBuilder();

    // Iterate through all enum constants and append their names to the StringBuilder
    for (DeviceType type : DeviceType.values()) {
      deviceTypes.append(type.name().toLowerCase()).append(", ");
    }

    // Remove the last comma and space if there are any sensor types
    if (!deviceTypes.isEmpty()) {
      deviceTypes.setLength(deviceTypes.length() - 2);
    }

    return deviceTypes.toString();
  }
}
