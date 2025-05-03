package com.growtech.api.services.users;

import com.growtech.api.dtos.UserInfo;
import com.growtech.api.dtos.requests.UserRegisterDto;
import com.growtech.api.entities.User;
import com.growtech.api.enums.UserRole;
import com.growtech.api.exceptions.CustomException;
import com.growtech.api.repositories.UserRepository;
import com.growtech.api.utils.JwtUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

@Service
public class UserServiceImpl implements  UserService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, JwtUtil jwtUtil) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  @Override
  public Mono<User> findByEmail(String email) {
    return this.userRepository.findByEmail(email);
  }

  @Override
  public Mono<User> findByUsername(String username) {
    return this.userRepository.findByUsername(username);
  }

  @Override
  public Mono<User> create(UserRegisterDto userRegisterDTO) {
    User user = new User();
    user.setEmail(userRegisterDTO.email());
    user.setUsername(userRegisterDTO.username());
    // Encrypt the password before saving it
    user.setPassword(passwordEncoder.encode(userRegisterDTO.password()));

    return userRepository.save(user);
  }

  @Override
  public Mono<Tuple2<List<UserInfo>, Long>> getAllUsersPaginated(PageRequest pageRequest) {
    return Mono.zip(
      // Fetch all users with pagination
      this.userRepository.findAllBy(pageRequest)
        .collectList()
        // Map the users to UserInfo DTOs
        .map(users -> users.stream()
          .map(User::getUserInfoDto)
          .toList()
        ),
      this.userRepository.count()
    );
  }

  @Override
  public Flux<String> getAllUserEmails() {
    return userRepository.findAllByRolesNotContains(List.of(UserRole.ADMIN, UserRole.SUPPORT))
      .map(EmailProjection::getEmail);
  }

  @Override
  public Mono<UserInfo> getUserById(String userId) {
    // Find the user by ID
    return userRepository.findUserById(userId)
      // If user is not found, throw a custom exception
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "User not found")))
        // Map the user to UserInfo DTO
      .map(User::getUserInfoDto);
  }

  @Override
  public Boolean passwordMatches(String rawPassword, String encodedPassword) {
    return passwordEncoder.matches(rawPassword, encodedPassword);
  }
}
