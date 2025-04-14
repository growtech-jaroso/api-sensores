package com.proyecto.apisensores.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

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
}
