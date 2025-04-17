package com.proyecto.apisensores.services.plantations;

import com.proyecto.apisensores.entities.Plantation;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public interface PlantationService {
  Flux<Plantation> getAllPlantationsByUserIdPaginated(String userId, String page, String limit);
}
