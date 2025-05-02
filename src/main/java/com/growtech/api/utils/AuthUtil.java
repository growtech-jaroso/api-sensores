package com.growtech.api.utils;

import com.growtech.api.entities.User;
import com.growtech.api.enums.UserRole;
import com.growtech.api.exceptions.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import reactor.core.publisher.Mono;

import java.util.Arrays;

public class AuthUtil {
  /**
   * Get the authenticated user from the security context.
   * @return Mono<User> containing the authenticated user.
   */
  public static Mono<User> getAuthUser() {
    return ReactiveSecurityContextHolder.getContext()
      .map(SecurityContext::getAuthentication)
      .cast(UsernamePasswordAuthenticationToken.class)
      .map(auth -> (User) auth.getPrincipal())
      // If not user throw an unauthorized exception, this should never happen
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.UNAUTHORIZED, "Unauthorized")));
  }

  public static Mono<Void> checkIfUserHaveRoles(UserRole... roles) {
    return getAuthUser()
      .filter(user -> Arrays.stream(roles).anyMatch(role -> user.getRoles().contains(role)))
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.FORBIDDEN, "Forbidden")))
      .then();
  }
}
