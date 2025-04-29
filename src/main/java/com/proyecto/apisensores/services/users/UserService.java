package com.proyecto.apisensores.services.users;

import com.proyecto.apisensores.dtos.requests.UserRegisterDto;
import com.proyecto.apisensores.entities.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface UserService {
  Mono<User> findByEmail(String email);
  Mono<User> findByUsername(String username);
  Boolean passwordMatches(String rawPassword, String encodedPassword);
  Mono<User> create(UserRegisterDto userRegisterDTO);
  Flux<String> getAllUserEmails();
}
