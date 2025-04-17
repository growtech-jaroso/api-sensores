package com.proyecto.apisensores.annotations;

import com.proyecto.apisensores.validators.PasswordMatchesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
public @interface PasswordMatches {
  String message() default "The passwords do not match"; // Error message
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
  String password() default "password"; // The name of the password field
  String confirmPassword() default "confirmPassword"; // The name of the confirmation password field
}
