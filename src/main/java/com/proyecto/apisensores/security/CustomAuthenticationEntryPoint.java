package com.proyecto.apisensores.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.proyecto.apisensores.responses.error.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CustomAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
  private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Override
  public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(HttpStatus.UNAUTHORIZED);
    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized");

    logger.info(ex.getMessage());

    try {
      OBJECT_MAPPER.registerModule(new JavaTimeModule()); // Register JavaTimeModule to handle Java 8 date/time types
      OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Avoids serialization of dates as strings
      byte[] bytes = OBJECT_MAPPER.writeValueAsBytes(error);
      DataBuffer buffer = response.bufferFactory().wrap(bytes);
      return response.writeWith(Mono.just(buffer));
    } catch (JsonProcessingException e) {
      logger.error("JsonProcessingException: {}", e.getMessage());
      return response.setComplete(); // fallback
    }
  }
}
