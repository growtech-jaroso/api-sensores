package com.growtech.api.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.growtech.api.dtos.requests.EditPlantationDto;
import com.growtech.api.dtos.requests.PlantationDto;
import com.growtech.api.enums.PlantationStatus;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import  lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Document("plantations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Plantation extends Model  {
  private String name;

  private String country;

  private String province;

  private String city;

  private String type;

  @Field(name = "owner_id")
  @JsonProperty("owner_id")
  private String ownerId; // ID of the user who owns the plantation

  private PlantationStatus status; // Status of the plantation

  @Indexed
  private List<String> managers; // List of users IDs who are managers of the plantation

  @Field(name = "central_coordinates")
  @JsonProperty("central_coordinates")
  private Coordinate centralCoordinates; // Central coordinate of the plantation

  @Field(name = "perimeter_coordinates")
  @JsonProperty("perimeter_coordinates")
  private List<Coordinate> perimeterCoordinates; // Perimeter coordinates of the plantation

  @Field(name = "map_url")
  @JsonProperty("map_url")
  private String mapUrl; // URL of the map associated with the plantation

  public Plantation(PlantationDto plantationDto, User user) {
    super();
    this.name = plantationDto.name();
    this.status = PlantationStatus.ONLINE;
    this.country = plantationDto.country();
    this.province = plantationDto.province();
    this.city = plantationDto.city();
    this.type = plantationDto.type();
    this.ownerId = user.getId();
    this.managers = List.of(user.getId());
    this.centralCoordinates = null; // This should be set later when the plantation is created with coordinates;
  }

  /**
   * Add a new assistant to the plantation
   * @param managerId String ID of the user to be added as an assistant
   */
  public void addNewManager(String managerId) {
    this.managers.add(managerId);
  }

  /**
   * Remove an assistant from the plantation
   * @param managerId String ID of the user to be removed as an assistant
   */
  public void removeManager(String managerId) {
    this.managers.remove(managerId);
  }

  /**
   * Soft delete the plantation by setting the isDeleted field to true
   * and clearing the ownerId and managers fields.
   */
  public void softDelete() {
    this.isDeleted = true;
    this.ownerId = "";
    this.managers.clear();
  }

  /**
   * Edit the plantation with the new information
   * @param plantationDto PlantationDto containing the new information
   */
  public void edit(EditPlantationDto plantationDto) {
    this.name = plantationDto.name();
    this.type = plantationDto.type();
    this.updatedAt = LocalDateTime.now();
  }
}
