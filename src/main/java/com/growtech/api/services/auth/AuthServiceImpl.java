package com.growtech.api.services.auth;

import com.growtech.api.dtos.AuthInfo;
import com.growtech.api.dtos.requests.UserLoginDto;
import com.growtech.api.dtos.requests.passwords.UserRegisterDto;
import com.growtech.api.entities.User;
import com.growtech.api.exceptions.CustomException;
import com.growtech.api.services.users.UserService;
import com.growtech.api.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthServiceImpl implements AuthService {

  private final UserService userService;
  private final JwtUtil jwtUtil;

  public AuthServiceImpl(UserService userService, JwtUtil jwtUtil) {
    this.userService = userService;
    this.jwtUtil = jwtUtil;
  }

  @Override
  public Mono<AuthInfo> registerUser(UserRegisterDto userRegisterDTO) {
    return checkEmailNotTaken(userRegisterDTO.email())
      .then(checkUsernameNotTaken(userRegisterDTO.username()))
      .then(userService.create(userRegisterDTO))
      .map(this::generateAuthInfo);
  }

  @Override
  public Mono<AuthInfo> loginUser(UserLoginDto userLoginDTO) {
    return userService.findByEmail(userLoginDTO.email())
      // Verify if the password matches with the database password
      .filter(user -> this.userService.passwordMatches(userLoginDTO.password(), user.getPassword()))
      .map(this::generateAuthInfo)
      // If the user is not found or the password does not match, return an error
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.UNAUTHORIZED, "Invalid email or password")));
  }

  /**
   * Check if the email is already taken
   * @param email Email to check
   * @return Mono<Void> if the email is not taken
   */
  private Mono<Void> checkEmailNotTaken(String email) {
    return this.userService.findByEmail(email)
      .flatMap(u -> Mono.error(new CustomException(HttpStatus.UNAUTHORIZED, "Email already exists")))
      .then();
  }

  /**
   * Check if the username is already taken
   * @param username Username to check
   * @return Mono<Void> if the username is not taken
   */
  private Mono<Void> checkUsernameNotTaken(String username) {
    return userService.findByUsername(username)
      .flatMap(u -> Mono.error(new CustomException(HttpStatus.UNAUTHORIZED, "Username already exists")))
      .then();
  }

  /**
   * Generate the auth info
   * @param user User to generate the auth info
   * @return AuthInfo with the token, username and email
   */
  private AuthInfo generateAuthInfo(User user) {
    String token = jwtUtil.generateToken(user);
    return user.getAuthInfo(token);
  }
}
