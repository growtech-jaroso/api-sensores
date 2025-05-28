package com.growtech.api.services.plantations;

import com.growtech.api.dtos.requests.EditPlantationDto;
import com.growtech.api.dtos.requests.PlantationManagerDto;
import com.growtech.api.dtos.requests.PlantationDto;
import com.growtech.api.dtos.responses.OwnerInfo;
import com.growtech.api.entities.Plantation;
import com.growtech.api.entities.Sensor;
import com.growtech.api.entities.User;
import com.growtech.api.enums.UserRole;
import com.growtech.api.exceptions.CustomException;
import com.growtech.api.repositories.plantation.PlantationRepository;
import com.growtech.api.repositories.sensor.SensorRepository;
import com.growtech.api.repositories.user.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
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
  public Mono<Tuple2<List<Plantation>, Long>> getAllPlantationsByUserPaginated(
    User user,
    String plantationSearchFilter,
    String plantationStatus,
    PageRequest pageRequest
  ) {
    if (plantationStatus == null) {
      return user.canViewAnything()
        ? Mono.zip(
        this.plantationRepository.findAllByFiltersAndIsDeletedIsFalse(
          plantationSearchFilter,
          pageRequest
        ).collectList(),
        this.plantationRepository.countAllByFiltersAndIsDeletedIsFalse(
          plantationSearchFilter
        )
      )
        : Mono.zip(
        this.plantationRepository.findAllByManagersContainingAndFiltersAndIsDeletedIsFalse(user.getId(), plantationSearchFilter, pageRequest)
          .collectList(),
        this.plantationRepository.countAllByManagersContainingAndFiltersAndIsDeletedIsFalse(user.getId(), plantationSearchFilter)
      );
    } else {
      return user.canViewAnything()
        ? Mono.zip(
        this.plantationRepository.findAllByFiltersAndPlantationStatusAndIsDeletedIsFalse(plantationSearchFilter, plantationStatus, pageRequest)
          .collectList(),
        this.plantationRepository.countAllByFiltersAndPlantationStatusAndIsDeletedIsFalse(plantationSearchFilter, plantationStatus)
      )
        : Mono.zip(
        this.plantationRepository.findAllByManagersContainingAndFiltersAndPlantationStatusAndIsDeletedIsFalse(user.getId(), plantationSearchFilter, plantationStatus, pageRequest)
          .collectList(),
        this.plantationRepository.countByManagersContainingAndFiltersAndPlantationStatusAndIsDeletedIsFalse(user.getId(), plantationSearchFilter, plantationStatus)
      );
    }
  }

  @Override
  public Mono<Tuple2<List<OwnerInfo>, Long>> getAllPlantationsOwners(PageRequest pageRequest) {
    // Get all plantations owners ids
    Flux<String> ownersIds = this.plantationRepository.findAllByIsDeletedIsFalse().map(OwnerIdProjection::getOwnerId);

    // Get all plantations owners by ids and not admin or support roles
    return ownersIds.collectList()
      .flatMap(ids ->
        Mono.zip(
          this.userRepository.findAllByIdInAndRoleNotInAndIsDeletedIsFalse(ids, List.of(UserRole.ADMIN, UserRole.SUPPORT), pageRequest)
            .collectList(),
          this.userRepository.countAllByIdInAndRoleNotInAndIsDeletedIsFalse(ids, List.of(UserRole.ADMIN, UserRole.SUPPORT))
        )
      );
  }

  @Override
  public Mono<Plantation> createPlantation(PlantationDto plantationDto) {
    // Retrieve the user by email
    Mono<User> user = this.userRepository.findByEmailAndRoleNotIn(plantationDto.userEmail(), List.of(UserRole.ADMIN, UserRole.SUPPORT))
      // If user not exists, throw  bad request exception
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "User email not found")));

    // Create a new plantation object
    Mono<Plantation> plantation = user.map(u -> new Plantation(plantationDto, u));

    // Check if the plantation name already exists
    return plantation.flatMap(pl -> plantationRepository
        .existsPlantationByNameAndOwnerIdAndIsDeletedIsFalse(plantationDto.name(), pl.getOwnerId()))
      .flatMap(exists -> {
        // If exists, throw bad request exception
        if (exists) return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Plantation name already exists for this owner"));
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
      Mono<User> newManagerUser = this.userRepository.findByEmailAndRoleNotIn(plantationManagerDto.managerEmail(), List.of(UserRole.ADMIN, UserRole.SUPPORT))
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
      Mono<User> newManagerUser = this.userRepository.findByEmailAndRoleNotIn(plantationManagerDto.managerEmail(), List.of(UserRole.ADMIN, UserRole.SUPPORT))
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
          .peek(Sensor::softDelete)
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

  @Override
  public Mono<Plantation> editPlantation(User user, String plantationId, EditPlantationDto plantationDto) {
    // Get the plantation by id if not exists, throw bad request exception
    Mono<Plantation> plantation = this.plantationRepository.findPlantationsByIdAndIsDeletedIsFalse(plantationId)
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Plantation not found")));

    // Check if the user is the owner of the plantation or has admin role
    return plantation.flatMap(pl -> {
      if (!pl.getOwnerId().equals(user.getId()) && !user.isAdmin()) {
        return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "Forbidden"));
      }

      // Check if the name is already taken by another plantation of the same owner and not the edited plantation
      return this.plantationRepository
        .existsPlantationByIdNotAndNameAndOwnerIdAndIsDeletedIsFalse(plantationId, plantationDto.name(), pl.getOwnerId())
        .flatMap(exists -> {
          if (exists) return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Plantation name already exists for this owner"));
          pl.edit(plantationDto);
          return this.plantationRepository.save(pl);
        });
    });
  }

  @Override
  public Mono<List<Plantation>> getPlantationsByUserId(String userId) {
    // Get the user by id if not exists, throw bad request exception
    Mono<User> user = this.userRepository.findUserById(userId)
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "User not found")));

    // Get all plantations by user id
    return user.flatMapMany(u -> this.plantationRepository.findAllByOwnerIdAndIsDeletedIsFalse(u.getId()))
      .collectList();
  }
}
