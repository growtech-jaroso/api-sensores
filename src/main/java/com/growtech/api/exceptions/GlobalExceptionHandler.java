package com.growtech.api.exceptions;

import com.growtech.api.responses.Response;
import com.growtech.api.responses.error.ErrorResponse;
import com.growtech.api.responses.error.ValidationErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(CustomException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleCustomException(CustomException e){
    logger.error("CustomException: {}", e.getMessage());
    ErrorResponse errorResponse = new ErrorResponse(e.getStatus(), e.getMessage());

    return Response.builder(e.getStatus(), errorResponse);
  }

  @ExceptionHandler(ValidationException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleValidationExceptions(ValidationException ex) {
    ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse(
      HttpStatus.BAD_REQUEST,
      ex.getMessage(),
      ex.getErrors()
    );

    return Response.builder(HttpStatus.BAD_REQUEST, validationErrorResponse);
  }

  @ExceptionHandler(AuthenticationException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleAuthenticationException(AuthenticationException e) {
    logger.error("AuthenticationException: {}", e.getMessage());
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized");
    return Response.builder(HttpStatus.UNAUTHORIZED, errorResponse);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleNoResourceFoundException(NoResourceFoundException e) {
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND,  "Resource not found");
    return Response.builder(HttpStatus.NOT_FOUND, errorResponse);
  }

  @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleNotFoundException(ChangeSetPersister.NotFoundException ex) {
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND,  "Resource not found");
    return Response.builder(HttpStatus.NOT_FOUND, errorResponse);
  }

  @ExceptionHandler(RuntimeException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleRuntimeException(Exception e){
    logger.error("RuntimeException: {}", e.getMessage());
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
    return Response.builder(HttpStatus.INTERNAL_SERVER_ERROR, errorResponse);
  }
}
