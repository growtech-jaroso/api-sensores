package com.proyecto.apisensores.handlers;

import com.proyecto.apisensores.dtos.UserLoginDto;
import com.proyecto.apisensores.dtos.UserRegisterDto;
import com.proyecto.apisensores.entities.User;
import com.proyecto.apisensores.responses.Response;
import com.proyecto.apisensores.responses.error.ErrorResponse;
import com.proyecto.apisensores.services.users.UserService;
import com.proyecto.apisensores.utils.JwtUtil;
import com.proyecto.apisensores.validators.ObjectValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class AuthHandler {

  private final UserService userService;
  private final JwtUtil jwtUtil;
  private final ObjectValidator objectValidator;

  public AuthHandler(UserService userService, JwtUtil jwtUtil, ObjectValidator objectValidator) {
    this.userService = userService;
    this.jwtUtil = jwtUtil;
    this.objectValidator = objectValidator;
  }

  public Mono<ServerResponse> login(ServerRequest request) {
    return request.bodyToMono(UserLoginDto.class).doOnNext(objectValidator::validate)
      .flatMap(dto -> {
        System.out.println("Login request: " + dto);
          // Find user by email
          return this.userService.findByEmail(dto.email())
            // Verify if the password matches with the database password
            .filter(user -> this.userService.passwordMatches(dto.password(), user.getPassword()) )
            .flatMap(user -> {
              System.out.println("User found: " + user.getId());
              // If matches, generate a token and return it
              String token = jwtUtil.generateToken(user);

              // Return the response with the user info
              return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(token);
            })
            // If the user is not found or the password does not match return UNAUTHORIZED response
            .switchIfEmpty(
              ServerResponse.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid credentials"))
            );
        }
      );
  }

  public Mono<ServerResponse> register(ServerRequest request) {
    return request.bodyToMono(UserRegisterDto.class)
      .flatMap(dto ->
        // Verify if user already exists
        this.userService.findByEmail(dto.email())
          .flatMap(existingUser ->
            // If exists return unauthorized response
            ServerResponse.badRequest()
              .contentType(MediaType.APPLICATION_JSON)
              .bodyValue("El usuario ya existe"))
          .switchIfEmpty(
            // If not exists, save user
            ServerResponse.ok()
              .contentType(MediaType.APPLICATION_JSON)
              .body(this.userService.save(dto), User.class)
          )
      );
  }

}
