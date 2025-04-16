package com.proyecto.apisensores.entities;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("nodes_coordinates")
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NodeCoordinate extends Model {
  private Double latitude;
  private Double longitude;

  @Field("node_id")
  @Indexed
  private String nodeId;

  public NodeCoordinate(Double latitude, Double longitude, String nodeId) {
    super();
    this.latitude = latitude;
    this.longitude = longitude;
    this.nodeId = nodeId;
  }
}
