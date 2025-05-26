package com.growtech.api.dtos.mqtt;

import com.growtech.api.enums.PlantationStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record PlantationStatusDto(
  @NotBlank
  String status
) {
  public PlantationStatus toPlantationStatus() {
    PlantationStatus plantationStatus = PlantationStatus.convertFromString(this.status);
    if (plantationStatus == null) {
      log.error("Invalid plantation status: {}", this.status);
      return null;
    }

    return plantationStatus;
  }
}
