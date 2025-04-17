package com.proyecto.apisensores.entities;

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

  public Plantation(String name, String country, String province, String city, String type, String ownerId) {
    super();
    this.name = name;
    this.country = country;
    this.province = province;
    this.city = city;
    this.type = type;
    this.ownerId = ownerId;
    this.users = List.of(ownerId);
  }
}
