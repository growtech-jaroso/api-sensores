package com.growtech.api.exceptions;

import org.springframework.http.HttpStatus;

public class EmptyBody extends RuntimeException {

  private final HttpStatus status = HttpStatus.BAD_REQUEST;

  public EmptyBody() {
    super("Expected request body but received empty body");
  }
}
