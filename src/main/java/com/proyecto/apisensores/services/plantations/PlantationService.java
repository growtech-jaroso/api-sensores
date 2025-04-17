package com.proyecto.apisensores.services.plantations;

import com.proyecto.apisensores.entities.Plantation;
import com.proyecto.apisensores.entities.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface PlantationService {
  Flux<Plantation> getAllPlantationsByUserPaginated(User user, PageRequest pageRequest);
  Mono<Long> getTotalPlantationsByUser(User user);
}
