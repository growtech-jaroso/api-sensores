package com.proyecto.apisensores.utils;

import com.proyecto.apisensores.entities.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import reactor.core.publisher.Mono;

public class AuthUtil {
  /**
   * Get the authenticated user from the security context.
   * @return Mono<User> containing the authenticated user.
   */
  public static Mono<User> getAuthUser() {
    return ReactiveSecurityContextHolder.getContext()
      .map(SecurityContext::getAuthentication)
      .cast(UsernamePasswordAuthenticationToken.class)
      .map(auth -> (User) auth.getPrincipal());
  }
}
