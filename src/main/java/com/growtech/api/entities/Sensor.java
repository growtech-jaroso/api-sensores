package com.growtech.api.entities;

import com.growtech.api.dtos.requests.SensorDto;
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

  private SensorUnit unit;

  @Field("plantation_id")
  @Indexed
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
    this.plantationId = plantationId;
    this.thresholdMinAlert = 0.0;
    this.thresholdMaxAlert = 0.0;
    this.coordinates = new ArrayList<>();
  }

  public void softDelete() {
    this.isDeleted = true;
  }
}
