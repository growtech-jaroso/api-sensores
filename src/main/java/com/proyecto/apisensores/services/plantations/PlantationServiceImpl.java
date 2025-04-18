package com.proyecto.apisensores.services.plantations;

import com.proyecto.apisensores.dtos.requests.PlantationDto;
import com.proyecto.apisensores.entities.Plantation;
import com.proyecto.apisensores.entities.User;
import com.proyecto.apisensores.enums.UserRole;
import com.proyecto.apisensores.exceptions.CustomException;
import com.proyecto.apisensores.repositories.PlantationRepository;
import com.proyecto.apisensores.repositories.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class PlantationServiceImpl implements PlantationService {
  private final PlantationRepository plantationRepository;
  private final UserRepository userRepository;

  public PlantationServiceImpl(PlantationRepository plantationRepository, UserRepository userRepository) {
    this.plantationRepository = plantationRepository;
    this.userRepository = userRepository;
  }

  @Override
  public Flux<Plantation> getAllPlantationsByUserPaginated(User user, PageRequest pageRequest) {
    return user.canViewAnything()
      ? this.plantationRepository.findAllBy(pageRequest)
      : this.plantationRepository.findAllByUsersContaining(user.getId(), pageRequest);
  }

  public Mono<Long> getTotalPlantationsByUser(User user) {
    return user.canViewAnything()
      ? this.plantationRepository.count()
      : this.plantationRepository.countAllByUsersContaining(user.getId());
  }

  @Override
  public Mono<Plantation> createPlantation(PlantationDto plantationDto) {
    // Retrieve the user by email
    Mono<User> user = this.userRepository.findByEmailAndRolesNotContains(plantationDto.userEmail(), List.of(UserRole.ADMIN, UserRole.SUPPORT))
      // If user not exists, throw  bad request exception
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "User email not exists")));

    // Create a new plantation object
    Mono<Plantation> plantation = user.map(u -> new Plantation(plantationDto, u));

    // Check if the plantation name already exists
    return this.plantationRepository.existsPlantationByName(plantationDto.name())
      .flatMap(exists -> {
        // If exists, throw bad request exception
        if (exists) return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Plantation name already exists"));
        // If not exists, save the plantation
        return plantation.flatMap(this.plantationRepository::save);
      });

  }
}
