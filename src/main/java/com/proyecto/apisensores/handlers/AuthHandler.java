package com.proyecto.apisensores.handlers;

import com.proyecto.apisensores.dtos.AuthInfo;
import com.proyecto.apisensores.dtos.UserLoginDto;
import com.proyecto.apisensores.dtos.UserRegisterDto;
import com.proyecto.apisensores.responses.success.DataResponse;
import com.proyecto.apisensores.services.auth.AuthService;
import com.proyecto.apisensores.validators.ObjectValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class AuthHandler {

  private final AuthService authService;
  private final ObjectValidator objectValidator;

  public AuthHandler(AuthService authService, ObjectValidator objectValidator) {
    this.authService = authService;
    this.objectValidator = objectValidator;
  }

  public Mono<ServerResponse> login(ServerRequest request) {
    return request.bodyToMono(UserLoginDto.class)
      .doOnNext(objectValidator::validate)
      .flatMap(this.authService::loginUser)
      .flatMap(authInfo -> {
        // Create a DataResponse object with the status and userLoginDto
        DataResponse<AuthInfo> response = new DataResponse<>(HttpStatus.OK, authInfo);
        // Return the response with the user info
        return ServerResponse.status(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(response);
      });
  }

  public Mono<ServerResponse> register(ServerRequest request) {
    return request.bodyToMono(UserRegisterDto.class).doOnNext(objectValidator::validate)
      .flatMap(this.authService::registerUser)
      .flatMap(authInfo -> {
        // Create a DataResponse object with the status and authInfo
        DataResponse<AuthInfo> response = new DataResponse<>(HttpStatus.CREATED, authInfo);
        // Return the response with the user info
        return ServerResponse.status(HttpStatus.CREATED)
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(response);
      });
  }
}
