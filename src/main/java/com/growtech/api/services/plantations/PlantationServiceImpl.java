package com.growtech.api.services.plantations;

import com.growtech.api.dtos.requests.PlantationManagerDto;
import com.growtech.api.dtos.requests.PlantationDto;
import com.growtech.api.entities.Plantation;
import com.growtech.api.entities.User;
import com.growtech.api.enums.UserRole;
import com.growtech.api.exceptions.CustomException;
import com.growtech.api.repositories.PlantationRepository;
import com.growtech.api.repositories.SensorRepository;
import com.growtech.api.repositories.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

@Service
public class PlantationServiceImpl implements PlantationService {
  private final PlantationRepository plantationRepository;
  private final UserRepository userRepository;
  private final SensorRepository sensorRepository;

  public PlantationServiceImpl(PlantationRepository plantationRepository, UserRepository userRepository, SensorRepository sensorRepository) {
    this.plantationRepository = plantationRepository;
    this.userRepository = userRepository;
    this.sensorRepository = sensorRepository;
  }

  @Override
  public Mono<Tuple2<List<Plantation>, Long>> getAllPlantationsByUserPaginated(User user, PageRequest pageRequest) {
    // Check if the user is associated with the plantation, doing with zip is more efficient because will be asynchronous
    return user.canViewAnything()
      ? Mono.zip(
        this.plantationRepository.findAllByIsDeletedIsFalse(pageRequest).collectList(),
        this.getTotalPlantationsByUser(user)
      )
      : Mono.zip(
        this.plantationRepository.findAllByManagersContainingAndIsDeletedIsFalse(user.getId(), pageRequest).collectList(),
        this.getTotalPlantationsByUser(user)
      );
  }

  private Mono<Long> getTotalPlantationsByUser(User user) {
    return user.canViewAnything()
      ? this.plantationRepository.countAllByIsDeletedIsFalse()
      : this.plantationRepository.countAllByManagersContainingAndIsDeletedIsFalse(user.getId());
  }

  @Override
  public Mono<Plantation> createPlantation(PlantationDto plantationDto) {
    // Retrieve the user by email
    Mono<User> user = this.userRepository.findByEmailAndRolesNotContains(plantationDto.userEmail(), List.of(UserRole.ADMIN, UserRole.SUPPORT))
      // If user not exists, throw  bad request exception
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "User email not found")));

    // Create a new plantation object
    Mono<Plantation> plantation = user.map(u -> new Plantation(plantationDto, u));

    // Check if the plantation name already exists
    return this.plantationRepository.existsPlantationByNameAndIsDeletedIsFalse(plantationDto.name())
      .flatMap(exists -> {
        // If exists, throw bad request exception
        if (exists) return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Plantation name already exists"));
        // If not exists, save the plantation
        return plantation.flatMap(this.plantationRepository::save);
      });
  }

  @Override
  public Mono<String> addPlantationManager(User authUser, String plantationId, PlantationManagerDto plantationManagerDto) {
    // Get the plantation by id if not exists, throw bad request exception
    Mono<Plantation> plantation = this.plantationRepository.findPlantationsByIdAndIsDeletedIsFalse(plantationId)
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Plantation not found")));

    return plantation.flatMap(pl -> {
      // Check if the user email exists and is user only, if not exists, throw bad request exception
      Mono<User> newManagerUser = this.userRepository.findByEmailAndRolesNotContains(plantationManagerDto.managerEmail(), List.of(UserRole.ADMIN, UserRole.SUPPORT))
        .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "User email not found")));

      return newManagerUser.flatMap(user -> {
        // Check if the user is the owner of the plantation or has admin role
        if (!pl.getOwnerId().equals(authUser.getId()) && !authUser.isAdmin()) {
          return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "Forbidden"));
        }
        // Add the user to the plantation and save the plantation
        pl.addNewManager(user.getId());

        // Save the plantation with the new manager
        return this.plantationRepository.save(pl)
          .flatMap(savedPlantation -> Mono.just("Manager added successfully"));
      });
    });
  }

  @Override
  public Mono<String> deletePlantationManager(User authUser, String plantationId, PlantationManagerDto plantationManagerDto) {
    // Get the plantation by id if not exists, throw bad request exception
    Mono<Plantation> plantation = this.plantationRepository.findPlantationsByIdAndIsDeletedIsFalse(plantationId)
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Plantation not found")));

    return plantation.flatMap(pl -> {
      // Check if the user email exists and is user only, if not exists, throw bad request exception
      Mono<User> newManagerUser = this.userRepository.findByEmailAndRolesNotContains(plantationManagerDto.managerEmail(), List.of(UserRole.ADMIN, UserRole.SUPPORT))
        .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "User email not found")));

      return newManagerUser.flatMap(user -> {
        if (user.getId().equals(pl.getOwnerId())) {
          return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Plantation owner cannot be removed from managers"));
        }

        // Check if the user is the owner of the plantation or has admin role
        if (!pl.getOwnerId().equals(authUser.getId()) && !authUser.isAdmin()) {
          return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "Forbidden"));
        }
        // Remove the manager from the plantation
        pl.removeManager(user.getId());

        // Save the plantation with the new manager
        return this.plantationRepository.save(pl)
          .flatMap(savedPlantation -> Mono.just("Manager deleted successfully"));
      });
    });
  }

  @Override
  public Mono<String> deletePlantation(String plantationId) {
    // Get the plantation by id if not exists, throw bad request exception
    Mono<Plantation> plantation = this.plantationRepository.findPlantationsByIdAndIsDeletedIsFalse(plantationId)
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Plantation not found")));

    return plantation.flatMap(pl ->
      // Get all sensors by plantation id
      this.sensorRepository.getAllByPlantationIdAndIsDeletedIsFalse(plantationId)
        // Set all sensors as deleted
        .collectList().map(sensors -> sensors
          .stream()
          .peek(sensor -> sensor.setIsDeleted(true))
          .toList()
        )
        // Soft delete all sensors of the plantation
        .flatMap(sensors -> this.sensorRepository.saveAll(sensors).collectList())
        // Soft delete the plantation
        .flatMap(sensors -> {
          pl.softDelete();
          return this.plantationRepository.save(pl);
        })
        .flatMap(deletedPlantation -> Mono.just("Plantation and his sensors deleted successfully"))
    );
  }
}
