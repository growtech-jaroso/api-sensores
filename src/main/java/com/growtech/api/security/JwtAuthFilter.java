package com.growtech.api.security;

import com.growtech.api.utils.JwtUtil;
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

    String token = jwtUtil.getTokenFromHeader(authHeader);

    // If token is not or not valid, continue with the chain
    if (token == null || !jwtUtil.isValidToken(token)) {
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
