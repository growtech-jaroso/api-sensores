package com.proyecto.apisensores.entities;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NodeCoordinate extends Model {
  private Double latitude;
  private Double longitude;
}
