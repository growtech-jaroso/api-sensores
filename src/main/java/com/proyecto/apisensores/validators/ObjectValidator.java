package com.proyecto.apisensores.validators;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.proyecto.apisensores.exceptions.ValidationException;
import com.proyecto.apisensores.responses.error.FieldError;
import jakarta.validation.ConstraintViolation;
import org.springframework.stereotype.Component;

import jakarta.validation.Validator;

import java.lang.reflect.Field;
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
        .map(error -> new FieldError(
          this.getJsonPropertyName(object, error.getPropertyPath().toString()),
          error.getMessage())
        )
        .toList();

      // Throw a ValidationException with the list of FieldErrors
      throw new ValidationException(fieldErrors);
    }
  }

  private String getJsonPropertyName(Object target, String fieldName) {
    if (target == null) return null;
    try {
      Field field = target.getClass().getDeclaredField(fieldName);
      JsonProperty annotation = field.getAnnotation(JsonProperty.class);
      return annotation != null ? annotation.value() : null;
    } catch (NoSuchFieldException e) {
      return fieldName;
    }
  }
}
