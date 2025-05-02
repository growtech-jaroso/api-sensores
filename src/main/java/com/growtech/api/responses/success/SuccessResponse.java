package com.growtech.api.responses.success;

import com.growtech.api.responses.Response;
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
