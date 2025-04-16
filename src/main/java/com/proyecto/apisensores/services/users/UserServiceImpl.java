package com.proyecto.apisensores.services.users;

import com.proyecto.apisensores.dtos.UserLoginDto;
import com.proyecto.apisensores.dtos.UserRegisterDto;
import com.proyecto.apisensores.entities.User;
import com.proyecto.apisensores.repositories.UserRepository;
import com.proyecto.apisensores.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements  UserService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;

  public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, JwtUtil jwtUtil) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    this.jwtUtil = jwtUtil;
  }

  public Mono<User> findByEmail(String email) {
    return null;
  }

  public Mono<User> findByUsername(String username) {
    return this.userRepository.findByUsername(username);
  }

  private Mono<User> save(UserRegisterDto userRegisterDTO) {
    User user = new User();
    user.setEmail(userRegisterDTO.email());
    user.setUsername(userRegisterDTO.username());
    // Encrypt the password before saving it
    user.setPassword(passwordEncoder.encode(userRegisterDTO.password()));

    return userRepository.save(user);

  }

  private Boolean passwordMatches(String rawPassword, String encodedPassword) {
    return passwordEncoder.matches(rawPassword, encodedPassword);
  }

  @Override
  public Mono<ServerResponse> register(ServerRequest request) {
    return request.bodyToMono(UserRegisterDto.class)
      .flatMap(dto ->
        // Verify if user already exists
        this.findByUsername(dto.username())
          .flatMap(existingUser ->
            // If exists return unauthorized response
            ServerResponse.badRequest()
              .contentType(MediaType.APPLICATION_JSON)
              .bodyValue("El usuario ya existe"))
          .switchIfEmpty(
            // If not exists, save user
            ServerResponse.ok()
              .contentType(MediaType.APPLICATION_JSON)
              .body(this.save(dto), User.class)
          )
      );
  }

  @Override
  public Mono<ServerResponse> login(ServerRequest request) {
    return request.bodyToMono(UserLoginDto.class)
      .flatMap(dto -> {
          // Busca al usuario por username
          return this.findByUsername(dto.username())
            // Verifica que la contraseña proporcionada coincida con la almacenada
            .filter(user -> this.passwordMatches(dto.password(), user.getPassword()) )
            .flatMap(user -> {
              // If matches, generate a token and return it
              String token = jwtUtil.generateToken(user);

              // Return the response with the user info
              return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(token);
            })
            // If the user is not found or the password does not match return UNAUTHORIZED
            .switchIfEmpty(ServerResponse.status(HttpStatus.UNAUTHORIZED).build());
        }
      );
  }
}
