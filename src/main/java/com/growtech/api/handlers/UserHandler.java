package com.growtech.api.handlers;

import com.growtech.api.enums.UserRole;
import com.growtech.api.responses.success.DataResponse;
import com.growtech.api.services.users.UserService;
import com.growtech.api.utils.AuthUtil;
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
}
