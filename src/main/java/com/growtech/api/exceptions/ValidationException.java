package com.growtech.api.exceptions;

import com.growtech.api.responses.error.FieldError;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {
  private final HttpStatus status = HttpStatus.BAD_REQUEST;
  private final List<FieldError> errors;

  public ValidationException(List<FieldError> errors) {
    super("Something went wrong");
    this.errors = errors;
  }
}
