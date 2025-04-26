package com.proyecto.apisensores.validators;

import com.proyecto.apisensores.annotations.IsSensorType;
import com.proyecto.apisensores.enums.SensorType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SensorTypeValidator implements ConstraintValidator<IsSensorType, String> {

  private String message;

  @Override
  public void initialize(final IsSensorType constraintAnnotation) {
    this.message = constraintAnnotation.message();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    // Check if the dto is null and return false
    if (value == null) return false;

    // If the value is not a valid SensorType, return false and add a violation
    if (SensorType.convertFromString(value) == null) {
      context.buildConstraintViolationWithTemplate(
        this.message + ", valid types are: " + SensorType.getAllSensorTypes()
        )
        .addConstraintViolation()
        .disableDefaultConstraintViolation();
      return false;
    }
    // If is a correct SensorType, return true
    return true;
  }
}
