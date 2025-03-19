package com.proyecto.apisensores.entities;

import com.proyecto.apisensores.enums.SensorType;
import com.proyecto.apisensores.enums.SensorUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("sensors")
@NoArgsConstructor
@Getter
@Setter
public class Sensor {
  private SensorType type;

  private SensorUnit unit;

  @Field("node_id")
  @Indexed
  private String nodeId;

  @Field("threshold_alert")
  private Double thresholdAlert;

  public Sensor(SensorType type, SensorUnit unit, String nodeId, Double thresholdAlert) {
    super();
    this.type = type;
    this.unit = unit;
    this.nodeId = nodeId;
    this.thresholdAlert = thresholdAlert;
  }
}
