package com.proyecto.apisensores.router;

import com.proyecto.apisensores.security.CustomAccessDeniedHandler;
import com.proyecto.apisensores.security.CustomAuthenticationEntryPoint;
import com.proyecto.apisensores.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
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

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
@EnableWebFluxSecurity
public class AppRouter implements WebFluxConfigurer {

  @Value("${app.security.cors.allowed-origins}")
  private String[] allowedOrigins;

  @Bean
  public RouterFunction<ServerResponse> appRoutes(
    PlantationRouter plantationRouter,
    AuthRouter authRouter
  ) {
    return RouterFunctions.route()
      // Base API prefix
      .path("/api", builder -> builder
        .path("/plantations", plantationRouter::plantationRoutes) // Routes for plantations
        .path("/auth", authRouter::authRoutes) // Routes for auth
      )
      .build();
  }

  /**
   * Security configuration for the application
   * @param http ServerHttpSecurity
   * @param jwtFilter JwtAuthFilter
   * @return SecurityWebFilterChain
   */
  @Bean
  SecurityWebFilterChain webHttpSecurity(
    ServerHttpSecurity http,
    JwtAuthFilter jwtFilter,
    CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
    CustomAccessDeniedHandler customAccessDeniedHandler
  ) {
    http
      .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/api/**"))
      .authorizeExchange(exchanges -> exchanges
        .pathMatchers("/api/auth/**").permitAll()// Public routes
        .anyExchange().authenticated()
      )
      .exceptionHandling(exceptionHandlingSpec -> {
        exceptionHandlingSpec
          .authenticationEntryPoint(customAuthenticationEntryPoint)
          .accessDeniedHandler(customAccessDeniedHandler);
      })
      .addFilterBefore(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
      .csrf(ServerHttpSecurity.CsrfSpec::disable)
      .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
      .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable);

    return http.build();
  }

  /**
   * CORS configuration for the application
   * @param registry registry of CORS
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
      .allowedOrigins(allowedOrigins) // Add domains from which requests are accepted
      .allowedMethods("GET", "POST", "PUT", "DELETE")
      .allowCredentials(true).maxAge(3600);
  }
}
