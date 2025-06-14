package com.growtech.api.validators;

import com.growtech.api.annotations.IsSensorUnit;
import com.growtech.api.enums.SensorUnit;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SensorUnitValidator implements ConstraintValidator<IsSensorUnit, String> {

  private String message;

  @Override
  public void initialize(final IsSensorUnit constraintAnnotation) {
    this.message = constraintAnnotation.message();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    // Check if the dto is null and return false
    if (value == null) return false;

    // If the value is not a valid SensorType, return false and add a violation
    if (SensorUnit.convertFromString(value) == null) {
      context.buildConstraintViolationWithTemplate(
        this.message + ", valid sensor units are: " + SensorUnit.getAllSensorUnits()
        )
        .addConstraintViolation()
        .disableDefaultConstraintViolation();
      return false;
    }
    // If is a correct SensorType, return true
    return true;
  }
}
