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
  public RouterFunction<ServerResponse> appRoutes(PlantationRouter plantationRouter) {
    return RouterFunctions.route()
      // Base API prefix
      .path("/api", builder -> builder
        .nest(accept(MediaType.APPLICATION_JSON), nestedBuilder ->
          nestedBuilder.path("/products", plantationRouter::plantationRoutes) // Routes for products
        )
      )
      .build();
  }

  /**
   * Configuración de seguridad
   * Añadimos la seguridad a las rutas que empiecen por /api/**
   * Permitimos acceso a las rutas de autenticación/registro sin autenticación
   * @param http
   * @return
   */
  @Bean
  SecurityWebFilterChain webHttpSecurity(ServerHttpSecurity http, JwtAuthFilter jwtFilter) {
    http
      .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/api/**"))
      .authorizeExchange(exchanges -> exchanges
        .pathMatchers("/api/auth/**").permitAll()  // Rutas públicas para autenticación/registro
        .anyExchange().authenticated()
      )
      .addFilterBefore(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
      .csrf(ServerHttpSecurity.CsrfSpec::disable)
      .httpBasic(withDefaults());
    return http.build();
  }

  /**
   * Configuración Cors, viene de implementar el interfaz WebFluxConfigurer y sobreescribir el método addCorsMappings
   * @param registry
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
      .allowedOrigins("http://javierprofe.com")   //Aquí ponemos el dominio desde el que aceptamos peticiones
      .allowedMethods("GET", "POST", "PUT", "DELETE")
      .allowCredentials(true).maxAge(3600);

    // Add more mappings...
  }
}
