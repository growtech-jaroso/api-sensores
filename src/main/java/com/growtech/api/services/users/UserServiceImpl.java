package com.growtech.api.services.users;

import com.growtech.api.dtos.requests.passwords.UserEditDto;
import com.growtech.api.dtos.responses.UserInfo;
import com.growtech.api.dtos.requests.passwords.ChangePasswordDto;
import com.growtech.api.dtos.requests.passwords.UserRegisterDto;
import com.growtech.api.entities.User;
import com.growtech.api.enums.UserRole;
import com.growtech.api.exceptions.CustomException;
import com.growtech.api.repositories.plantation.PlantationRepository;
import com.growtech.api.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
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

  @Value("${app.users.admin.email}")
  private String firstAdminEmail;
  @Value("${app.users.admin.username}")
  private String firstAdminUsername;
  @Value("${app.users.admin.password}")
  private String firstAdminPassword;

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
    // Encrypt the password before saving it
    String encryptedPassword = passwordEncoder.encode(userRegisterDTO.password());

    User user = new User(userRegisterDTO, encryptedPassword);

    return userRepository.save(user);
  }

  @Override
  public Mono<Tuple2<List<UserInfo>, Long>> getAllUsersPaginated(String searchFilter, UserRole roleFilter, PageRequest pageRequest) {

    // Check if the role filter is null to do a query without it
    if (roleFilter == null) {
      return Mono.zip(
        // Get all users with pagination
        this.userRepository.findAllByUsernameContainsIgnoreCaseOrEmailContainsIgnoreCase(searchFilter, searchFilter, pageRequest)
          .collectList()
          // Map the users to UserInfo DTOs
          .map(users -> users.stream()
            .map(User::getUserInfoDto)
            .toList()
          ),
        this.userRepository.countAllByUsernameContainsIgnoreCaseOrEmailContainsIgnoreCase(searchFilter, searchFilter)
      );
    }

    // If role filter is not null, do a query using it
    return Mono.zip(
      // Get all users with pagination
      this.userRepository.findMatchingUsers(searchFilter, roleFilter, pageRequest)
        .collectList()
        // Map the users to UserInfo DTOs
        .map(users -> users.stream()
          .map(User::getUserInfoDto)
          .toList()
        ),
      this.userRepository.countByUsernameOrEmailAndRole(searchFilter, roleFilter)
    );
  }

  @Override
  public Flux<String> getAllUserEmails() {
    return userRepository.findAllByRoleNotIn(List.of(UserRole.ADMIN, UserRole.SUPPORT)).map(EmailProjection::getEmail);
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

  @Override
  public Mono<UserInfo> getUserById(String userId) {
    return this.userRepository.findUserById(userId)
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "User not found")))
      .map(User::getUserInfoDto);
  }

  @Override
  public Mono<String> changePassword(User user, ChangePasswordDto changePasswordDto) {
    // Check if the old password is correct
    if (!this.passwordMatches(changePasswordDto.oldPassword(), user.getPassword())) {
      return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Old password is incorrect"));
    }

    // Encrypt the new password
    String encryptedPassword = passwordEncoder.encode(changePasswordDto.newPassword());
    user.setPassword(encryptedPassword);

    // Save the user and return a success message
    return userRepository.save(user)
      .map(u -> "Password changed successfully");
  }

  @Override
  public Mono<UserInfo> editUser(String userId, UserEditDto userEditDto) {
    // Check if the user id exists
    return this.userRepository.findById(userId)
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "User not found")))
      // Check if the email is already taken by another user
      .flatMap(user -> this.userRepository.findUserByIdIsNotAndEmail(userId, userEditDto.email())
          .flatMap(u -> Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Email already exists")))
          // Check if the username is already taken by another user
          .then(this.userRepository.findUserByIdIsNotAndUsername(userId, userEditDto.username()))
          .flatMap(u -> Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Username already exists")))
          .then(Mono.just(user))
          .map(u -> {
            // If the password is not empty, update the password
            if (!userEditDto.password().isEmpty()) u.setPassword(this.passwordEncoder.encode(userEditDto.password()));

            // Edit the user with the new information
            user.edit(userEditDto);
            return u;
          })
      )
      .flatMap(this.userRepository::save)
      .map(User::getUserInfoDto);
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
