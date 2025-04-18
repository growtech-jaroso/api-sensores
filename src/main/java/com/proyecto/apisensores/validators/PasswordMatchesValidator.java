package com.proyecto.apisensores.validators;

import com.proyecto.apisensores.annotations.PasswordMatches;
import com.proyecto.apisensores.dtos.requests.UserRegisterDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserRegisterDto> {
  private String message;
  @Override
  public void initialize(final PasswordMatches constraintAnnotation) {
    message = constraintAnnotation.message();
  }

  @Override
  public boolean isValid(final UserRegisterDto dto, final ConstraintValidatorContext context) {
    // Check if the dto is null and return false
    if (dto == null) return false;

    // If the password is null or empty, return false and add a violation
    if (dto.password() == null || !dto.password().equals(dto.confirmPassword())) {
      context.buildConstraintViolationWithTemplate(message)
        .addPropertyNode("confirm_password")
        .addConstraintViolation()
        .disableDefaultConstraintViolation();
      return false;
    }
    // If the password matches, return true
    return true;
  }
}
