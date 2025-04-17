package com.proyecto.apisensores.services.plantations;

import com.proyecto.apisensores.entities.Plantation;
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
  public Flux<Plantation> getAllPlantationsByUserIdPaginated(String userId, PageRequest pageRequest) {
    // TODO: Validate that the auth user can access the plantations
    return this.plantationRepository.findAllByUsersContaining(userId, pageRequest);
  }

  public Mono<Long> getTotalPlantationsByUserId(String userId) {
    return this.plantationRepository.countAllByUsersContaining(userId);
  }
}
