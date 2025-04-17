package com.proyecto.apisensores.services.plantations;

import com.proyecto.apisensores.entities.Plantation;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface PlantationService {
  Flux<Plantation> getAllPlantationsByUserIdPaginated(String userId, PageRequest pageRequest);
  Mono<Long> getTotalPlantationsByUserId(String userId);
}
