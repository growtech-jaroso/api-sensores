package com.proyecto.apisensores.services.users;

import com.proyecto.apisensores.dtos.UserRegisterDto;
import com.proyecto.apisensores.entities.User;
import com.proyecto.apisensores.repositories.UserRepository;
import com.proyecto.apisensores.utils.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements  UserService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, JwtUtil jwtUtil) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  public Mono<User> findByEmail(String email) {
    return this.userRepository.findByEmail(email);
  }

  @Override
  public Mono<User> findByUsernameOrEmail(String username, String email) {
    return this.userRepository.findByUsernameOrEmail(username, email);
  }

  @Override
  public Mono<User> findByUsername(String username) {
    return this.userRepository.findByUsername(username);
  }

  public Mono<User> save(UserRegisterDto userRegisterDTO) {
    User user = new User();
    user.setEmail(userRegisterDTO.email());
    user.setUsername(userRegisterDTO.username());
    // Encrypt the password before saving it
    user.setPassword(passwordEncoder.encode(userRegisterDTO.password()));

    return userRepository.save(user);
  }

  @Override
  public Mono<Boolean> existsByEmail(String email) {
    return this.userRepository.existsByEmail(email);
  }

  @Override
  public Mono<Boolean> existsByUsername(String username) {
    return this.userRepository.existsByUsername(username);
  }

  public Boolean passwordMatches(String rawPassword, String encodedPassword) {
    return passwordEncoder.matches(rawPassword, encodedPassword);
  }
}
