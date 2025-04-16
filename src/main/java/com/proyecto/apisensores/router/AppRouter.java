package com.proyecto.apisensores.router;

import com.proyecto.apisensores.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
@EnableWebFluxSecurity
public class AppRouter implements WebFluxConfigurer {
  @Bean
  public RouterFunction<ServerResponse> appRoutes(
    PlantationRouter plantationRouter,
    AuthRouter authRouter
  ) {
    return RouterFunctions.route()
      // Base API prefix
      .path("/api", builder -> builder
        .nest(accept(MediaType.APPLICATION_JSON), nestedBuilder ->
          nestedBuilder
            .path("/plantations", plantationRouter::plantationRoutes) // Routes for plantations
            .path("/auth", authRouter::authRoutes) // Routes for auth
        )
      )
      .build();
  }

  /**
   * Configure security
   * We add security to routes that start with /api/auth/**
   * We allow access to the public routes without authentication
   */
  @Bean
  SecurityWebFilterChain webHttpSecurity(ServerHttpSecurity http, JwtAuthFilter jwtFilter) {
    http
      .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/api/**"))
      .authorizeExchange(exchanges -> exchanges
        .pathMatchers("/api/auth/**").permitAll()  // Public routes
        .anyExchange().authenticated()
      )
      .addFilterBefore(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
      .csrf(ServerHttpSecurity.CsrfSpec::disable)
      .httpBasic(withDefaults());
    return http.build();
  }

  /**
   * Configure Cors, is coming from WebFluxConfigurer interface and overriding the addCorsMappings method
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
      .allowedOrigins("https://javierprofe.com") // Add domains from which requests are accepted
      .allowedMethods("GET", "POST", "PUT", "DELETE")
      .allowCredentials(true).maxAge(3600);

    // Add more mappings...
  }
}
