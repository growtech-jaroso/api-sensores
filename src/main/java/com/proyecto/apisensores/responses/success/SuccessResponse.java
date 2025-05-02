package com.proyecto.apisensores.responses.success;

import com.proyecto.apisensores.responses.Response;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SuccessResponse extends Response {

  private final String message;

  public SuccessResponse(HttpStatus status, String message) {
    super(status);
    this.message = message;
  }
}
