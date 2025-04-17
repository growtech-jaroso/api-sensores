package com.proyecto.apisensores.services.plantations;

import com.proyecto.apisensores.dtos.PlantationDto;
import com.proyecto.apisensores.entities.Plantation;
import com.proyecto.apisensores.entities.User;
import com.proyecto.apisensores.repositories.PlantationRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PlantationServiceImpl implements PlantationService {
  private final PlantationRepository plantationRepository;

  public PlantationServiceImpl(PlantationRepository plantationRepository) {
    this.plantationRepository = plantationRepository;
  }

  @Override
  public Flux<Plantation> getAllPlantationsByUserPaginated(User user, PageRequest pageRequest) {
    return user.canViewAnything()
      ? this.plantationRepository.findAll()// pageRequest)
      : this.plantationRepository.findAllByUsersContaining(user.getId(), pageRequest);
  }

  public Mono<Long> getTotalPlantationsByUser(User user) {
    return user.canViewAnything()
      ? this.plantationRepository.count()
      : this.plantationRepository.countAllByUsersContaining(user.getId());
  }

  @Override
  public Mono<Plantation> createPlantation(PlantationDto dto, User user) {
    return null;
  }


}
