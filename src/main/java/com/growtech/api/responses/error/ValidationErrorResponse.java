package com.growtech.api.responses.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class ValidationErrorResponse extends ErrorResponse {
  private final List<FieldError> errors;

  public ValidationErrorResponse(HttpStatus status, String message, List<FieldError> errors) {
    super(status, message);
    this.errors = errors;
  }
}
