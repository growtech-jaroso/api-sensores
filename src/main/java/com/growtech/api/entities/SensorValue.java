package com.growtech.api.entities;

import lombok.Builder;
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
@Builder
public class SensorValue extends Model {
  private Double value;

  @Indexed
  private LocalDateTime timestamp;

  @Field("sensor_id")
  @Indexed
  private String sensorId;

  public SensorValue(Double value, LocalDateTime timestamp, String sensorId) {
    super();
    this.value = value;
    this.timestamp = timestamp;
    this.sensorId = sensorId;
  }
}
