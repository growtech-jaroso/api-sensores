package com.proyecto.apisensores.services.plantations;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public interface PlantationService {
  Mono<ServerResponse> getAllPlantationsByUser(ServerRequest request);
}
