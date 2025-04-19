package com.proyecto.apisensores.handlers;

import com.proyecto.apisensores.entities.User;
import com.proyecto.apisensores.responses.Response;
import com.proyecto.apisensores.utils.AuthUtil;
import com.proyecto.apisensores.utils.ParamsUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class SensorHandler {
  public Mono<ServerResponse> getAllSensorsByPlantation(ServerRequest request) {
    // Create a page request from the request parameters
    PageRequest pageRequest = ParamsUtil.getPageRequest(request);

    // Get the plantation id from the request
    String plantationId = request.pathVariable("plantation_id");

    // Retrieve the user from the request
    Mono<User> authUser = AuthUtil.getAuthUser();

    return Response.builder(HttpStatus.MOVED_PERMANENTLY).build();
  }
}
