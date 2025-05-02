package com.growtech.api.responses;

import com.growtech.api.responses.error.ErrorResponse;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Data
public abstract class Response {
  private final Integer status;
  private final LocalDateTime timestamp;

  public Response(HttpStatus status) {
    this.status = status.value();
    this.timestamp = LocalDateTime.now();
  }

  /**
   * Builder for creating a ServerResponse with the given status and content type
   * @param status the status of the response
   * @return a ServerResponse builder
   */
  public static ServerResponse.BodyBuilder builder(HttpStatus status) {
    return ServerResponse.status(status)
      .contentType(MediaType.APPLICATION_JSON);
  }

  public static Mono<ResponseEntity<ErrorResponse>> builder(HttpStatus status, ErrorResponse body) {
    return Mono.just(ResponseEntity.status(status).body(body));
  }
}
