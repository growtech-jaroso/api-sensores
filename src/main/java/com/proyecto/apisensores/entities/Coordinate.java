package com.proyecto.apisensores.entities;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Coordinate {
  private Double latitude;
  private Double longitude;
}
