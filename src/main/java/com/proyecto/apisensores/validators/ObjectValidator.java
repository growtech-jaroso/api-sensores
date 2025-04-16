package com.proyecto.apisensores.validators;

import com.proyecto.apisensores.exceptions.ValidationException;
import com.proyecto.apisensores.responses.error.FieldError;
import jakarta.validation.ConstraintViolation;
import org.springframework.stereotype.Component;

import jakarta.validation.Validator;

import java.util.List;
import java.util.Set;

@Component
public class ObjectValidator {
  private final Validator validator;

  public ObjectValidator(Validator validator) {
    this.validator = validator;
  }

  /**
   * Validates an object using the validator.
   * @param object the object to validate
   * @throws ValidationException if the object is not valid
   * @param <T> the type of the object
   */
  public <T> void validate(T object) {
    // Validate the object using the validator
    Set<ConstraintViolation<T>> errors = validator.validate(object);

    // If there are no errors, return the object
    if (!errors.isEmpty()) {
      // Map the errors to a list of FieldError
      List<FieldError> fieldErrors = errors.stream()
        .map(error -> new FieldError(error.getPropertyPath().toString(), error.getMessage()))
        .toList();

      // Throw a ValidationException with the list of FieldErrors
      throw new ValidationException(fieldErrors);
    }
  }
}
