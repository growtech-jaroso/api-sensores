package com.proyecto.apisensores.services.plantations;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class PlantationServiceImpl implements PlantationService {
  @Override
  public Mono<ServerResponse> getAllPlantationsByUser(ServerRequest request) {
    return null;
  }
}
