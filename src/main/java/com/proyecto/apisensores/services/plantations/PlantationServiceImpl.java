package com.proyecto.apisensores.services.plantations;

import com.proyecto.apisensores.entities.Plantation;
import com.proyecto.apisensores.repositories.PlantationRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class PlantationServiceImpl implements PlantationService {
  private final PlantationRepository plantationRepository;

  public PlantationServiceImpl(PlantationRepository plantationRepository) {
    this.plantationRepository = plantationRepository;
  }

  @Override
  public Flux<Plantation> getAllPlantationsByUserIdPaginated(String userId, String page, String limit) {
    return null;
  }
}
