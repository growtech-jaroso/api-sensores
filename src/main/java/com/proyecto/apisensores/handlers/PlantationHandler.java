package com.proyecto.apisensores.handlers;

import com.proyecto.apisensores.entities.Plantation;
import com.proyecto.apisensores.responses.Response;
import com.proyecto.apisensores.responses.success.paginated.PaginatedResponse;
import com.proyecto.apisensores.services.plantations.PlantationService;
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

  public Mono<ServerResponse> getAllPlantationsByUser(ServerRequest request) {
    // Retrieve the page and limit parameters from the request
    String page = request.queryParam("page").orElse("1");
    String limit = request.queryParam("limit").orElse("10");

    // Convert the page and limit to integers
    Integer pageNumber = ParamsUtil.checkPage(page);
    Integer limitNumber = ParamsUtil.checkLimit(limit);

    PageRequest pageRequest = PageRequest.of(pageNumber, limitNumber);

    // Retrieve the user ID of the authenticated user
    String userId = request.headers().header("user").getFirst();

    // Retrieve the products from the database
    Flux<Plantation> products = this.plantationService.getAllPlantationsByUserIdPaginated(userId, pageRequest);

    // Creat
    return products.collectList()
      .zipWith(this.plantationService.getTotalPlantationsByUserId(userId))
      .flatMap( tuple -> Response.builder(HttpStatus.OK)
        .bodyValue(new PaginatedResponse<>(
          HttpStatus.OK,
          tuple.getT2(),
          pageRequest,
          limitNumber,
          tuple.getT1()
        ))
      );
  }
}
