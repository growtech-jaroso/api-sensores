package com.growtech.api.validators;

import com.growtech.api.annotations.PasswordMatches;
import com.growtech.api.dtos.requests.passwords.PasswordsInterface;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, PasswordsInterface> {
  private String message;
  private String confirmPasswordField;
  @Override
  public void initialize(final PasswordMatches constraintAnnotation) {
    this.message = constraintAnnotation.message();
    this.confirmPasswordField = constraintAnnotation.confirmPassword();
  }

  @Override
  public boolean isValid(final PasswordsInterface dto, final ConstraintValidatorContext context) {
    // Check if the dto is null and return false
    if (dto == null) return false;

    // If the password is null or empty, return false and add a violation
    if (dto.getPassword() == null || !dto.getPassword().equals(dto.getConfirmPassword())) {
      context.buildConstraintViolationWithTemplate(this.message)
        .addPropertyNode(this.confirmPasswordField)
        .addConstraintViolation()
        .disableDefaultConstraintViolation();
      return false;
    }
    // If the password matches, return true
    return true;
  }
}
