package com.proyecto.apisensores.validators;

import com.proyecto.apisensores.annotations.IsSensorUnit;
import com.proyecto.apisensores.enums.SensorUnit;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SensorUnitValidator implements ConstraintValidator<IsSensorUnit, String> {
  private String propertyName;

  @Override
  public void initialize(final IsSensorUnit constraintAnnotation) {
    this.propertyName = constraintAnnotation.fieldName();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    // Check if the dto is null and return false
    if (value == null) return false;

    // If the value is not a valid SensorType, return false and add a violation
    if (SensorUnit.convertFromString(value) == null) {
      context.buildConstraintViolationWithTemplate(
        " Valid sensor units are: " + SensorUnit.getAllSensorUnits()
        )
        .addPropertyNode(this.propertyName)
        .addConstraintViolation()
        .disableDefaultConstraintViolation();
      return false;
    }
    // If is a correct SensorType, return true
    return true;
  }
}
