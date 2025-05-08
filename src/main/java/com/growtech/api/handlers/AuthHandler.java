package com.growtech.api.handlers;

import com.growtech.api.dtos.responses.AuthInfo;
import com.growtech.api.dtos.requests.UserLoginDto;
import com.growtech.api.dtos.requests.passwords.UserRegisterDto;
import com.growtech.api.enums.UserRole;
import com.growtech.api.exceptions.EmptyBody;
import com.growtech.api.responses.success.DataResponse;
import com.growtech.api.services.auth.AuthService;
import com.growtech.api.utils.AuthUtil;
import com.growtech.api.validators.ObjectValidator;
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
      .switchIfEmpty(Mono.error(new EmptyBody()))
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
    Mono<UserRegisterDto> userRegisterDto = request.bodyToMono(UserRegisterDto.class)
      .doOnNext(objectValidator::validate)
      .switchIfEmpty(Mono.error(new EmptyBody()));
    // Check if user is admin to register a new user
    return AuthUtil.checkIfUserHaveRoles(UserRole.ADMIN)
      .then(userRegisterDto)
      // Create a new user
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
