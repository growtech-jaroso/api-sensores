package com.proyecto.apisensores.handlers;

import com.proyecto.apisensores.entities.Plantation;
import com.proyecto.apisensores.entities.User;
import com.proyecto.apisensores.responses.Response;
import com.proyecto.apisensores.responses.success.paginated.PaginatedResponse;
import com.proyecto.apisensores.services.plantations.PlantationService;
import com.proyecto.apisensores.utils.AuthUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.proyecto.apisensores.utils.ParamsUtil;

@Component
public class PlantationHandler {

  private final PlantationService plantationService;

  public PlantationHandler(PlantationService plantationService) {
    this.plantationService = plantationService;
  }

  public Mono<ServerResponse> createPlantation(ServerRequest request) {
    // Implementation for creating a plantation
    return null;
  }

  public Mono<ServerResponse> getAllPlantationsByUser(ServerRequest request) {
    // Create a page request from the request parameters
    PageRequest pageRequest = ParamsUtil.getPageRequest(request);

    // Retrieve the user from the request
    Mono<User> authUser = AuthUtil.getAuthUser();

    // Get plantations by user the authenticated user
    return authUser.
      flatMap(user -> {
        // Get all plantations by user
        Flux<Plantation> userPlantations = this.plantationService.getAllPlantationsByUserPaginated(user, pageRequest);
        // Get the total number of plantations by user
        Mono<Long> totalUserPlantations = this.plantationService.getTotalPlantationsByUser(user);

        // Create a paginated response
        return userPlantations.collectList()
          .zipWith(totalUserPlantations)
          .flatMap(tuple -> Response.builder(HttpStatus.OK)
            .bodyValue(new PaginatedResponse<>(
              HttpStatus.OK,
              tuple.getT2(),
              pageRequest,
              tuple.getT1()
            ))
          );
      });
  }
}
