package com.growtech.api.validators;

import com.growtech.api.annotations.IsUserRole;
import com.growtech.api.enums.SensorUnit;
import com.growtech.api.enums.UserRole;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserRoleValidator implements ConstraintValidator<IsUserRole, String> {

  private String message;

  @Override
  public void initialize(IsUserRole constraintAnnotation) {
    this.message = constraintAnnotation.message();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    // Check if the dto is null and return false
    if (value == null) return false;

    // If the value is not a valid UserRole, return false and add a violation
    if (UserRole.convertFromString(value) == null) {
      context.buildConstraintViolationWithTemplate(
          this.message + ", valid user roles are: " + UserRole.getAllUserRoles()
        )
        .addConstraintViolation()
        .disableDefaultConstraintViolation();
      return false;
    }
    // If is a correct SensorType, return true
    return true;
  }
}
