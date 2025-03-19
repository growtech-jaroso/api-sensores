package com.proyecto.apisensores.responses.error;

import com.proyecto.apisensores.responses.Response;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse extends Response {
  protected final String message;

  public ErrorResponse(HttpStatus status, String message) {
    super(status);
    this.message = message;
  }
}
