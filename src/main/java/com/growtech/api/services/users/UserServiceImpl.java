package com.growtech.api.services.users;

import com.growtech.api.dtos.UserInfo;
import com.growtech.api.dtos.requests.UserRegisterDto;
import com.growtech.api.entities.Plantation;
import com.growtech.api.entities.User;
import com.growtech.api.enums.UserRole;
import com.growtech.api.exceptions.CustomException;
import com.growtech.api.repositories.PlantationRepository;
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
  private final PlantationRepository plantationRepository;

  public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, PlantationRepository plantationRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    this.plantationRepository = plantationRepository;
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
    return userRepository.findAllByRolesNotContains(List.of(UserRole.ADMIN, UserRole.SUPPORT)).map(EmailProjection::getEmail);
  }

  @Override
  public Mono<String> deleteUser(User authUser, String userId) {
    // Retrieve the user by ID
    Mono<User> user = this.userRepository.findById(userId)
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "User not found")));

    return user.flatMap(u -> {
      // Check if user is not the same as authUser
      if (u.getId().equals(authUser.getId())) {
        return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "You cannot delete yourself"));
      }

      // Check if user is owner of any plantation
      return plantationRepository.existsPlantationByOwnerIdAndIsDeletedIsFalse(u.getId())
        .flatMap(plExists -> {
          // If user is owner of any plantation, throw an error
          if (plExists) {
            return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "User is owner of a plantation"));
          }
          // Delete the user from all plantations where he is a manager
          return this.deleteUserFromPlantations(u)
            // Delete the user
            .then(this.userRepository.delete(u))
            // Return success message
            .then(Mono.just("User deleted successfully"));
        });
    });
  }

  /**
   * Delete a user from all plantations where they are a manager.
   * @param user The user to be deleted from plantations.
   * @return A Mono that completes when the user is removed from all plantations.
   */
  private Mono<Void> deleteUserFromPlantations(User user) {
    // Retrieve all plantations where the user is a manager
    return this.plantationRepository.findAllByManagersContainingAndIsDeletedIsFalse(user.getId())
      .collectList()
      .map(plantations -> {
          // Remove the user from the managers list of each plantation
          return plantations.stream()
            .peek(plantation -> plantation.removeManager(user.getId()))
            .toList();
        })
      .flatMap(plantations -> this.plantationRepository.saveAll(plantations).collectList())
      .then();
  }

  @Override
  public Boolean passwordMatches(String rawPassword, String encodedPassword) {
    return passwordEncoder.matches(rawPassword, encodedPassword);
  }
}
