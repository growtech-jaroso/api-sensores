package com.proyecto.apisensores.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class AppRouter {
  @Bean
  public RouterFunction<ServerResponse> appRoutes(ProductRouter productRouter) {
    return RouterFunctions.route()
      // Base API prefix
      .path("/api", builder -> builder
        .nest(accept(MediaType.APPLICATION_JSON), nestedBuilder ->
          nestedBuilder.path("/products", productRouter::productRoutes) // Routes for products
        )
      )
      .build();
  }
}
