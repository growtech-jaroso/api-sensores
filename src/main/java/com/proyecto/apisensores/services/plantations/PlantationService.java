package com.proyecto.apisensores.services.plantations;

import com.proyecto.apisensores.dtos.requests.PlantationAssistantDto;
import com.proyecto.apisensores.dtos.requests.PlantationDto;
import com.proyecto.apisensores.entities.Plantation;
import com.proyecto.apisensores.entities.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

@Service
public interface PlantationService {
  Mono<Tuple2<List<Plantation>, Long>> getAllPlantationsByUserPaginated(User user, PageRequest pageRequest);
  Mono<Plantation> createPlantation(PlantationDto plantationDto);
  Mono<String> addPlantationAssistant(User user, String plantationId, PlantationAssistantDto plantationAssistantDto);
}
