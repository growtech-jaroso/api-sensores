package com.growtech.api.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.growtech.api.dtos.requests.SensorDto;
import com.growtech.api.enums.ActuatorStatus;
import com.growtech.api.enums.DeviceType;
import com.growtech.api.enums.SensorType;
import com.growtech.api.enums.SensorUnit;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document("sensors")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Sensor extends Model {
  private SensorType type;

  @Field("device_type")
  @JsonProperty("device_type")
  private DeviceType deviceType;

  private SensorUnit unit;

  private ActuatorStatus status;

  @Field("plantation_id")
  @Indexed
  @JsonProperty("plantation_id")
  private String plantationId;

  @Field("threshold_min_alert")
  private Double thresholdMinAlert;

  @Field("threshold_max_alert")
  private Double thresholdMaxAlert;

  private List<Coordinate> coordinates;

  public Sensor(SensorDto sensorDto, String plantationId) {
    super();
    this.unit = SensorUnit.convertFromString(sensorDto.sensorUnit());
    this.type = SensorType.convertFromString(sensorDto.sensorType());
    this.deviceType = DeviceType.SENSOR;
    this.plantationId = plantationId;
    this.thresholdMinAlert = 0.0;
    this.thresholdMaxAlert = 0.0;
    this.coordinates = new ArrayList<>();
  }

  public Sensor(String plantationId) {
    super();
    this.unit = null;
    this.type = null;
    this.deviceType = DeviceType.ACTUATOR;
    this.status = ActuatorStatus.OFF;
    this.plantationId = plantationId;
    this.thresholdMinAlert = null;
    this.thresholdMaxAlert = null;
    this.coordinates = new ArrayList<>();
  }

  public void softDelete() {
    this.isDeleted = true;
  }
}
