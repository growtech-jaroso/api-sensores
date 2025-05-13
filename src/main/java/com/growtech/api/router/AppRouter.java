package com.growtech.api.router;

import com.growtech.api.security.CustomAccessDeniedHandler;
import com.growtech.api.security.CustomAuthenticationEntryPoint;
import com.growtech.api.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

@Configuration
@EnableWebFluxSecurity
public class AppRouter implements WebFluxConfigurer {

  @Value("${app.security.cors.allowed-origins}")
  private String[] allowedOrigins;

  @Bean
  public RouterFunction<ServerResponse> routes(
    PlantationRouter plantationRouter,
    AuthRouter authRouter,
    UserRouter userRouter,
    InformationRouter informationRouter
  ) {
    return RouterFunctions.route()
      // Base API prefix
      .path("/api", builder -> builder
        .path("/plantations", plantationRouter::plantationRoutes) // Routes for plantations
        .path("/users", userRouter::userRoutes) // Routes for users information
        .path("/auth", authRouter::authRoutes) // Routes for auth
        .path("/information", informationRouter::informationRoutes) // Routes for information
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
        .pathMatchers(HttpMethod.OPTIONS).permitAll() // CORS preflight requests
        .pathMatchers("/api/auth/**").permitAll() // Public routes
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
      .allowedHeaders("*")
      .allowedOrigins(allowedOrigins) // Add domains from which requests are accepted
      .allowedMethods("GET", "POST", "PUT", "DELETE")
      .allowCredentials(true).maxAge(3600);
  }
}
