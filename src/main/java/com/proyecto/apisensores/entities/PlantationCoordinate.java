package com.proyecto.apisensores.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("plantations_coordinates")
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PlantationCoordinate {
  private Double latitude;
  private Double longitude;

  @Field("plantation_id")
  @Indexed
  private String plantationId;

  public PlantationCoordinate(Double latitude, Double longitude, String plantationId) {
    super();
    this.latitude = latitude;
    this.longitude = longitude;
    this.plantationId = plantationId;
  }
}
