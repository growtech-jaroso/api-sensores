package com.proyecto.apisensores.handlers;

import com.proyecto.apisensores.dtos.UserRegisterDto;
import com.proyecto.apisensores.entities.User;
import com.proyecto.apisensores.utils.JwtUtil;
import com.proyecto.apisensores.services.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UserHandler {

  private final UserService userService;
  private final JwtUtil jwtUtil;

  public UserHandler(UserService userService, JwtUtil jwtUtil) {
    this.userService = userService;
    this.jwtUtil = jwtUtil;
  }


  public Mono<ServerResponse> register(ServerRequest request) {

    return request.bodyToMono(UserRegisterDto.class)
      .flatMap(dto ->
        // Verificamos si ya existe un usuario con ese nombre
        userService.findByUsername(dto.username())
          .flatMap(existingUser ->
            // Si existe, devolvemos una respuesta de error
            ServerResponse.badRequest()
              .contentType(MediaType.APPLICATION_JSON)
              .bodyValue("El usuario ya existe"))
          .switchIfEmpty(
            // Si no existe, lo guardamos
            ServerResponse.ok()
              .contentType(MediaType.APPLICATION_JSON)
              .body(userService.save(dto), User.class)
            //.bodyValue("Usuario registrado")
          )
      );
  }

  public Mono<ServerResponse> login(ServerRequest request) {

    return request.bodyToMono(UserLoginDTO.class)
      .flatMap(dto -> {
          // Busca al usuario por username
          return userService.findByUsername(dto.username())
            // Verifica que la contraseña proporcionada coincida con la almacenada
            .filter(user -> userService.passwordMatches(dto.password(), user.getPassword()) )
            .flatMap(user -> {
              // Si coincide, genera un token y lo retorna
              String token = jwtUtil.generateToken(user);

              // Retorna la respuesta con el usuario
              return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new AuthResponse(token));
            })
            // Si no se encontró el usuario o la contraseña no coincide, retorna UNAUTHORIZED
            .switchIfEmpty(ServerResponse.status(HttpStatus.UNAUTHORIZED).build());
        }
      );
  }

}
