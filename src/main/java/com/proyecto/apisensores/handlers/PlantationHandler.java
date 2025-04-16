package com.proyecto.apisensores.handlers;

import com.proyecto.apisensores.services.plantations.PlantationService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class PlantationHandler {

  private final PlantationService plantationService;

  public PlantationHandler(PlantationService plantationService) {
    this.plantationService = plantationService;
  }

  public Mono<ServerResponse> getAllPlantationsByUser(ServerRequest request) {
    return null;
  }
}
