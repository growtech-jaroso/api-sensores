package com.proyecto.apisensores.entities;

import com.proyecto.apisensores.dtos.requests.PlantationDto;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import  lombok.*;
import java.util.List;

@Document("plantations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Plantation extends Model  {
  @Indexed(unique = true)
  private String name; // Name field is indexed and unique

  private String country;

  private String province;

  private String city;

  private String type;

  private String ownerId; // ID of the user who owns the plantation

  private List<String> users; // List of users IDs associated with the plantation

  private List<Coordinate> coordinates; // List of coordinates associated with the plantation

  public Plantation(PlantationDto plantationDto, User user) {
    super();
    this.name = plantationDto.name();
    this.country = plantationDto.country();
    this.province = plantationDto.province();
    this.city = plantationDto.city();
    this.type = plantationDto.type();
    this.ownerId = user.getId();
    this.users = List.of(user.getId());
  }
}
