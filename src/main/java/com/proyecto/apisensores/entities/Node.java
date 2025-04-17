package com.proyecto.apisensores.entities;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document("nodes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Node extends Model {
  @Indexed(unique = true)
  private String name;

  @Field("plantation_id")
  @Indexed
  private String plantationId;

  List<NodeCoordinate> coordinates;

  public Node(String name, String plantationId) {
    super();
    this.name = name;
    this.plantationId = plantationId;
    this.coordinates = new ArrayList<>();
  }
}
