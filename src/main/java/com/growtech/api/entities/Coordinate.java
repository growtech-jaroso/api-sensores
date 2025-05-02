package com.growtech.api.entities;

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
