package com.growtech.api.entities;

import com.growtech.api.dtos.requests.CoordinateDto;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Coordinate {
  private String latitude;
  private String longitude;

  public Coordinate(CoordinateDto coordinateDto) {
    this.latitude = coordinateDto.latitude();
    this.longitude = coordinateDto.longitude();
  }
}
