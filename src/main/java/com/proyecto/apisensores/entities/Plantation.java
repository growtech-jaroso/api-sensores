package com.proyecto.apisensores.entities;

import com.proyecto.apisensores.dtos.PlantationDto;
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

  private List<PlantationCoordinate> coordinates; // List of coordinates associated with the plantation

  public Plantation(PlantationDto dto, User user) {
    super();
    this.name = name;
    this.country = country;
    this.province = province;
    this.city = city;
    this.type = type;
    this.ownerId = user.getId();
    this.users = List.of(user.getId());
  }
}
