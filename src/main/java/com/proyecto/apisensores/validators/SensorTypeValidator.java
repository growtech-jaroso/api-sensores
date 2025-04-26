package com.proyecto.apisensores.validators;

import com.proyecto.apisensores.annotations.IsSensorType;
import com.proyecto.apisensores.enums.SensorType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SensorTypeValidator implements ConstraintValidator<IsSensorType, String> {

  private String propertyName;

  @Override
  public void initialize(final IsSensorType constraintAnnotation) {
    this.propertyName = constraintAnnotation.fieldName();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    // Check if the dto is null and return false
    if (value == null) return false;

    // If the value is not a valid SensorType, return false and add a violation
    if (SensorType.convertFromString(value) == null) {
      context.buildConstraintViolationWithTemplate(
        "Invalid sensor type. Valid types are: " + SensorType.getAllSensorTypes()
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
