package com.proyecto.apisensores.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document("sensor_values")
@NoArgsConstructor
@Getter
@Setter
public class SensorValue extends Model {
  private Double value;

  private LocalDateTime date;

  @Field("node_id")
  @Indexed
  private String nodeId;

  public SensorValue(Double value, LocalDateTime date, String nodeId) {
    super();
    this.value = value;
    this.date = date;
    this.nodeId = nodeId;
  }
}
