package com.proyecto.apisensores.handlers;

import com.proyecto.apisensores.dtos.requests.PlantationAssistantDto;
import com.proyecto.apisensores.dtos.requests.PlantationDto;
import com.proyecto.apisensores.entities.Plantation;
import com.proyecto.apisensores.entities.User;
import com.proyecto.apisensores.enums.UserRole;
import com.proyecto.apisensores.responses.Response;
import com.proyecto.apisensores.responses.success.DataResponse;
import com.proyecto.apisensores.responses.success.paginated.PaginatedResponse;
import com.proyecto.apisensores.services.plantations.PlantationService;
import com.proyecto.apisensores.utils.AuthUtil;
import com.proyecto.apisensores.validators.ObjectValidator;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import com.proyecto.apisensores.utils.ParamsUtil;

import java.util.List;

@Component
public class PlantationHandler {

  private final PlantationService plantationService;
  private final ObjectValidator objectValidator;

  public PlantationHandler(PlantationService plantationService, ObjectValidator objectValidator) {
    this.plantationService = plantationService;
    this.objectValidator = objectValidator;
  }

  public Mono<ServerResponse> createPlantation(ServerRequest request) {
    // Check if user is authorized to create a plantation
    Mono<PlantationDto> plantationDto = AuthUtil.checkIfUserHaveRoles(UserRole.ADMIN)
      // Validate the request body
      .then(request.bodyToMono(PlantationDto.class).doOnNext(objectValidator::validate));


    // Create a new plantation and return in response
    return plantationDto
      .flatMap(this.plantationService::createPlantation)
      .flatMap(plantation -> Response
        .builder(HttpStatus.CREATED)
        .bodyValue(new DataResponse<>(HttpStatus.CREATED, plantation))
      );
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
        return this.plantationService
          .getAllPlantationsByUserPaginated(user, pageRequest)
          // Create a paginated response
          .flatMap(tuple -> Response.builder(HttpStatus.OK)
            .bodyValue(new PaginatedResponse<>(
              HttpStatus.OK,
              tuple.getT2(),
              pageRequest,
              tuple.getT1()
            )));
      });
  }

  public Mono<ServerResponse> addPlantationsAssistants(ServerRequest request) {
    // Create the plantation assistant dto from the request body
    Mono<PlantationAssistantDto> plantationAssistantDto = request.bodyToMono(PlantationAssistantDto.class)
      .doOnNext(objectValidator::validate);

    // Retrieve the user from the request

  }
}
