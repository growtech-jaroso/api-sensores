package com.growtech.api.handlers;

import com.growtech.api.dtos.requests.EditPlantationDto;
import com.growtech.api.dtos.requests.PlantationManagerDto;
import com.growtech.api.dtos.requests.PlantationDto;
import com.growtech.api.entities.User;
import com.growtech.api.enums.PlantationStatus;
import com.growtech.api.enums.UserRole;
import com.growtech.api.exceptions.EmptyBody;
import com.growtech.api.responses.Response;
import com.growtech.api.responses.success.DataResponse;
import com.growtech.api.responses.success.SuccessResponse;
import com.growtech.api.responses.success.paginated.PaginatedResponse;
import com.growtech.api.services.plantations.PlantationService;
import com.growtech.api.utils.AuthUtil;
import com.growtech.api.validators.ObjectValidator;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import com.growtech.api.utils.ParamsUtil;

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
      .then(
        request.bodyToMono(PlantationDto.class)
          .doOnNext(objectValidator::validate)
          .switchIfEmpty(Mono.error(new EmptyBody()))
      );


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
    // Get the plantation search filter from the request parameters
    String plantationSearchFilter = request.queryParam("search").orElse("");
    // Get the plantation filter for has alerts from the request parameters
    String plantationStatusStr = request.queryParam("status").orElse("");

    // Convert the plantation status string to an enum
    PlantationStatus plantationStatus = PlantationStatus.convertFromString(plantationStatusStr);

    if (plantationStatus == null) plantationStatusStr = "";

    String finalPlantationStatusStr = plantationStatusStr;

    // Retrieve the user from the request
    Mono<User> authUser = AuthUtil.getAuthUser();

    // Get plantations by user the authenticated user
    return authUser.
      flatMap(user -> {
        // Get all plantations by user
        return this.plantationService
          .getAllPlantationsByUserPaginated(
            user,
            plantationSearchFilter,
            finalPlantationStatusStr,
            pageRequest
          )
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

  public Mono<ServerResponse> getAllPlantationsOwners(ServerRequest request) {
    // Create a page request from the request parameters
    PageRequest pageRequest = ParamsUtil.getPageRequest(request);

    // Check if the user has the ADMIN role and return the owners of the plantations
    return AuthUtil.checkIfUserHaveRoles(UserRole.ADMIN, UserRole.SUPPORT)
      .then(
        this.plantationService.getAllPlantationsOwners(pageRequest)
          .flatMap(tuple -> Response.builder(HttpStatus.OK)
            .bodyValue(new PaginatedResponse<>(
              HttpStatus.OK,
              tuple.getT2(),
              pageRequest,
              tuple.getT1()
            ))
          )
      );
  }

  public Mono<ServerResponse> editPlantation(ServerRequest request) {
    // Get the plantation id from the request path
    String plantationId = request.pathVariable("plantation_id");

    // Get the edited plantation info from the request body
    Mono<EditPlantationDto> plantationDto = request.bodyToMono(EditPlantationDto.class)
      .doOnNext(objectValidator::validate)
      .switchIfEmpty(Mono.error(new EmptyBody()));

    // Retrieve the authenticated user
    Mono<User> authUser = AuthUtil.getAuthUser();

    return plantationDto.flatMap(plDto -> authUser
      .flatMap(user -> this.plantationService.editPlantation(user, plantationId, plDto))
      .flatMap(plantation -> Response
        .builder(HttpStatus.OK)
        .bodyValue(new DataResponse<>(HttpStatus.OK, plantation))
      ));
  }

  public Mono<ServerResponse> addPlantationManager(ServerRequest request) {
    // Get the plantation id from the request path
    String plantationId = request.pathVariable("plantation_id");

    // Create the plantation assistant dto from the request body
    Mono<PlantationManagerDto> plantationManagerDto = request.bodyToMono(PlantationManagerDto.class)
      .doOnNext(objectValidator::validate)
      .switchIfEmpty(Mono.error(new EmptyBody()));

    return plantationManagerDto
      // Get the authenticated user
      .flatMap(plantationManager -> AuthUtil.getAuthUser()
        // Add the plantation assistant to the plantation
        .flatMap(user -> this.plantationService.addPlantationManager(user, plantationId, plantationManager))
        // Return the response with the success message
        .flatMap(message -> Response
          .builder(HttpStatus.OK)
          .bodyValue(new SuccessResponse(HttpStatus.OK, message))
        ));
  }

  public Mono<ServerResponse> deletePlantationManager(ServerRequest request) {
    // Get the plantation id from the request path
    String plantationId = request.pathVariable("plantation_id");

    // Create the plantation assistant dto from the request body
    Mono<PlantationManagerDto> plantationManagerDto = request.bodyToMono(PlantationManagerDto.class)
      .doOnNext(objectValidator::validate)
      .switchIfEmpty(Mono.error(new EmptyBody()));

    return plantationManagerDto
      // Get the authenticated user
      .flatMap(plantationManager -> AuthUtil.getAuthUser()
        // Add the plantation assistant to the plantation
        .flatMap(user -> this.plantationService.deletePlantationManager(user, plantationId, plantationManager))
        // Return the response with the success message
        .flatMap(message -> Response
          .builder(HttpStatus.OK)
          .bodyValue(new SuccessResponse(HttpStatus.OK, message))
        ));
  }

  public Mono<ServerResponse> deletePlantation(ServerRequest request) {
    // Get the plantation id from the request path
    String plantationId = request.pathVariable("plantation_id");

    // Delete the plantation and return the response
    return AuthUtil.checkIfUserHaveRoles(UserRole.ADMIN)
      .then(this.plantationService.deletePlantation(plantationId))
      .flatMap(message -> Response
        .builder(HttpStatus.OK)
        .bodyValue(new SuccessResponse(HttpStatus.OK, message))
      );
  }

  // Get all plantations by user id
  public Mono<ServerResponse> getPlantationsByUserId(ServerRequest serverRequest) {
    String userId = serverRequest.pathVariable("user_id");

    // Check if the user has the ADMIN role
    return AuthUtil.checkIfUserHaveRoles(UserRole.ADMIN, UserRole.SUPPORT)
      .then(
        this.plantationService.getPlantationsByUserId(userId)
          .flatMap(plantations -> Response
            .builder(HttpStatus.OK)
            .bodyValue(new DataResponse<>(HttpStatus.OK, plantations))
          )
      );
    }

  public Mono<ServerResponse> getPlantationById(ServerRequest serverRequest) {
    String plantationId = serverRequest.pathVariable("plantation_id");

    // Get the user from the request
    return AuthUtil.getAuthUser()
      .flatMap(user -> this.plantationService.getPlantationById(plantationId, user)
          .flatMap(plantation -> Response
            .builder(HttpStatus.OK)
            .bodyValue(new DataResponse<>(HttpStatus.OK, plantation))
          )
      );
  }
}
