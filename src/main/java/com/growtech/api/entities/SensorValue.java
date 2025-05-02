package com.growtech.api.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
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
  @JsonProperty("reading_timestamp")
  @Field(name = "reading_timestamp")
  private LocalDateTime readingTimestamp;

  @Indexed
  @JsonProperty("sensor_id")
  @Field("sensor_id")
  private String sensorId;

  public SensorValue(Double value, LocalDateTime readingTimestamp, String sensorId) {
    super();
    this.value = value;
    this.readingTimestamp = readingTimestamp;
    this.sensorId = sensorId;
  }
}
