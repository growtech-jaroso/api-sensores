package com.growtech.api.handlers;

import com.growtech.api.dtos.requests.passwords.ChangePasswordDto;
import com.growtech.api.dtos.requests.passwords.UserEditDto;
import com.growtech.api.enums.UserRole;
import com.growtech.api.exceptions.CustomException;
import com.growtech.api.exceptions.EmptyBody;
import com.growtech.api.responses.Response;
import com.growtech.api.responses.success.DataResponse;
import com.growtech.api.responses.success.SuccessResponse;
import com.growtech.api.responses.success.paginated.PaginatedResponse;
import com.growtech.api.services.users.UserService;
import com.growtech.api.utils.AuthUtil;
import com.growtech.api.utils.ParamsUtil;
import com.growtech.api.validators.ObjectValidator;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UserHandler {

  private final UserService userService;
  private final ObjectValidator objectValidator;

  public UserHandler(UserService userService, ObjectValidator objectValidator) {
    this.userService = userService;
    this.objectValidator = objectValidator;
  }

  public Mono<ServerResponse> getAllUserEmails(ServerRequest request) {
    // Check if the user has the ADMIN role and return the emails of the users
    return AuthUtil.checkIfUserHaveRoles(UserRole.ADMIN)
      .then(Mono.defer(() -> userService.getAllUserEmails().collectList()))
      .flatMap(emails -> ServerResponse.ok().bodyValue(new DataResponse<>(HttpStatus.OK, emails)));
  }

  public Mono<ServerResponse> getAllUsers(ServerRequest request) {
    // Create a PageRequest object with default values
    PageRequest pageRequest = ParamsUtil.getPageRequest(request);
    // Get the username name from the request parameters
    String usernameFilter = request.queryParam("username").orElse("");
    // Get the email name from the request parameters
    String emailFilter = request.queryParam("email").orElse("");

    // Check if the user has the ADMIN role and return the users paginated
    return AuthUtil.checkIfUserHaveRoles(UserRole.ADMIN)
      .then(userService.getAllUsersPaginated(usernameFilter, emailFilter, pageRequest))
      .flatMap(tuple2 -> ServerResponse
        .ok()
        .bodyValue(new PaginatedResponse<>(
          HttpStatus.OK,
          tuple2.getT2(),
          pageRequest,
          tuple2.getT1()
        )));
  }

  public Mono<ServerResponse> deleteUser(ServerRequest request) {
    // Extract the user ID from the request path variable
    String userId = request.pathVariable("user_id");

    // Check if the user has the ADMIN role and delete the user
    return AuthUtil.checkIfUserHaveRoles(UserRole.ADMIN)
      .then(AuthUtil.getAuthUser())
      .flatMap(user -> userService.deleteUser(user, userId))
      .flatMap(message -> ServerResponse
        .ok()
        .bodyValue(new SuccessResponse(HttpStatus.OK, message)));
  }

  public Mono<ServerResponse> getUserById(ServerRequest request) {
    // Extract the user ID from the request path variable
    String userId = request.pathVariable("user_id");

    // Check if the user has the ADMIN role and return the user by ID
    return AuthUtil.checkIfUserHaveRoles(UserRole.ADMIN)
      .then(userService
        .getUserById(userId))
      .flatMap(userInfo -> ServerResponse.ok()
        .bodyValue(new DataResponse<>(HttpStatus.OK, userInfo)));
  }

  public Mono<ServerResponse> changePassword(ServerRequest request) {
    // Get the change password from the request body
    Mono<ChangePasswordDto> changePasswordDto = request.bodyToMono(ChangePasswordDto.class)
      .doOnNext(objectValidator::validate)
      .switchIfEmpty(Mono.error(new EmptyBody()));

    // Change the password of the user
    return changePasswordDto
      // Get the authenticated user
      .flatMap(changePassDto -> AuthUtil.getAuthUser()
          .flatMap(user -> this.userService.changePassword(user, changePassDto))
          .flatMap(message -> ServerResponse.ok()
            .bodyValue(new SuccessResponse(HttpStatus.OK, message)))
      );
  }

  public Mono<ServerResponse> editUser(ServerRequest request) {
    // Get the user id from the request path
    String userId = request.pathVariable("user_id");

    // Get the edited user info from the request body
    Mono<UserEditDto> userEditDto = request.bodyToMono(UserEditDto.class)
      .doOnNext(objectValidator::validate)
      .switchIfEmpty(Mono.error(new EmptyBody()));

    // Check if the user has the ADMIN role
    return AuthUtil.checkIfUserHaveRoles(UserRole.ADMIN)
      // Check if the auth user is the same as the user to be edited
      .then(AuthUtil.getAuthUser()
        .filter(u -> !u.getId().equals(userId))
        .switchIfEmpty(Mono.error(new CustomException(HttpStatus.FORBIDDEN, "You cannot edit yourself")))
      )
      // Edit the user
      .then(userEditDto.flatMap(userDto -> this.userService.editUser(userId, userDto)))
      .flatMap(userInfo -> ServerResponse.ok()
        .bodyValue(new DataResponse<>(HttpStatus.OK, userInfo))
      );
  }
}
