package com.proyecto.apisensores.entities;

import com.proyecto.apisensores.enums.SensorType;
import com.proyecto.apisensores.enums.SensorUnit;
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
public class Sensor {
  private SensorType type;

  private SensorUnit unit;

  @Field("plantation_id")
  @Indexed
  private String plantationId;

  @Field("threshold_alert")
  private Double thresholdAlert;

  private List<Coordinate> coordinates;

  public Sensor(SensorType type, SensorUnit unit, String plantationId, Double thresholdAlert) {
    super();
    this.type = type;
    this.unit = unit;
    this.plantationId = plantationId;
    this.thresholdAlert = thresholdAlert;
    this.coordinates = new ArrayList<>();
  }
}
