package com.proyecto.apisensores.security;

import com.proyecto.apisensores.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements WebFilter {
  private final JwtUtil jwtUtil;
  private final ReactiveUserDetailsService userDetailsService;

  public JwtAuthFilter(JwtUtil jwtUtil, ReactiveUserDetailsService userDetailsService) {
    this.jwtUtil = jwtUtil;
    this.userDetailsService = userDetailsService;
  }


  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

    // Get auth header from exchange
    String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

    // If not auth header, continue with the chain
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return chain.filter(exchange);
    }

    // Get token from auth header
    String token = authHeader.split(" ")[1];

    // If token is not valid, continue with the chain
    if (!jwtUtil.isValidToken(token)) {
      return chain.filter(exchange);
    }

    // Get email from token
    return Mono.justOrEmpty(jwtUtil.getUsernameFromToken(token))
      .flatMap(userDetailsService::findByUsername)
      .flatMap(userDetails -> {
        UsernamePasswordAuthenticationToken authToken =
          new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // Set authentication in the security context passing the auth token
        return chain.filter(exchange)
          .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authToken));
      });
  }


}
