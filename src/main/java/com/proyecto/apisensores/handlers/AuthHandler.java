package com.proyecto.apisensores.handlers;

import com.proyecto.apisensores.dtos.AuthInfo;
import com.proyecto.apisensores.dtos.UserLoginDto;
import com.proyecto.apisensores.dtos.UserRegisterDto;
import com.proyecto.apisensores.responses.error.ErrorResponse;
import com.proyecto.apisensores.responses.success.DataResponse;
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
          // Find user by email
          return this.userService.findByEmail(dto.email())
            // Verify if the password matches with the database password
            .filter(user -> this.userService.passwordMatches(dto.password(), user.getPassword()) )
            .flatMap(user -> {
              // If matches, generate a token and return it
              String token = jwtUtil.generateToken(user);

              // Return the response with the user info
              return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new DataResponse<>(HttpStatus.CREATED, new AuthInfo(token, user.getEmail(), user.getUsername())));
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
    return request.bodyToMono(UserRegisterDto.class).doOnNext(objectValidator::validate)
      .flatMap(dto -> {
        // Check if the user already exists by email
        return this.userService.findByEmail(dto.email())
          .flatMap(existingUser -> {
            // If exists, return UNAUTHORIZED response with error message
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, "User email already exists");
            return ServerResponse.status(HttpStatus.UNAUTHORIZED)
              .contentType(MediaType.APPLICATION_JSON)
              .bodyValue(errorResponse);
          })
          .switchIfEmpty(
            // If not exists, check if the user already exists by username
            this.userService.findByUsername(dto.username())
              .flatMap(existingUser -> {
                // If exists, return UNAUTHORIZED response with error message
                ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Username already exists");
                return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                  .contentType(MediaType.APPLICATION_JSON)
                  .bodyValue(errorResponse);
              })
              .switchIfEmpty(
                // If not exists, save user
                this.userService.save(dto)
                  // Return the username, email and a generated token
                  .flatMap(user -> {
                    String token = jwtUtil.generateToken(user);
                    return ServerResponse.ok()
                      .contentType(MediaType.APPLICATION_JSON)
                      .bodyValue(new DataResponse<>(HttpStatus.CREATED, new AuthInfo(token, dto.email(), dto.username())));
                  })
              )
          );
      });
  }
}
