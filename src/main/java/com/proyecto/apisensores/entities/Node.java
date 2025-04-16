package com.proyecto.apisensores.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("nodes")
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Node extends Model {
  @Indexed(unique = true)
  private String name;

  @Field("plantation_id")
  @Indexed
  private String plantationId;

  public Node(String name, String plantationId) {
    super();
    this.name = name;
    this.plantationId = plantationId;
  }
}
