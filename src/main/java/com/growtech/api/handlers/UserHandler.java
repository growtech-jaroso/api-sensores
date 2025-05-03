package com.growtech.api.handlers;

import com.growtech.api.enums.UserRole;
import com.growtech.api.responses.success.DataResponse;
import com.growtech.api.responses.success.SuccessResponse;
import com.growtech.api.responses.success.paginated.PaginatedResponse;
import com.growtech.api.services.users.UserService;
import com.growtech.api.utils.AuthUtil;
import com.growtech.api.utils.ParamsUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UserHandler {

  private final UserService userService;

  public UserHandler(UserService userService) {
    this.userService = userService;
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

    // Check if the user has the ADMIN role and return the users paginated
    return AuthUtil.checkIfUserHaveRoles(UserRole.ADMIN)
      .then(userService.getAllUsersPaginated(pageRequest))
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
}
