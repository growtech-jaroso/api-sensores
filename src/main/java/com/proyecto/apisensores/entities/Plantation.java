package com.proyecto.apisensores.entities;

import jakarta.persistence.Column;
import org.springframework.data.mongodb.core.mapping.Document;

import  lombok.*;
import java.util.List;

@Document(collation = "plantations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Plantation extends Model  {

  @Column(unique = true)
  private String name; // Hace que el campo "name" sea único
  private String country;
  private String province;
  private String city;
  private String type;

  public Plantation(String name, String country, String province, String city, String type) {
    super();
    this.name = name;
    this.country = country;
    this.province = province;
    this.city = city;
    this.type = type;
  }

  // Relación con usuarios (Lista de IDs de usuarios asociados)
  private List<String> users;

}
