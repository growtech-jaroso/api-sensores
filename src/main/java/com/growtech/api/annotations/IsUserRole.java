package com.growtech.api.annotations;

import com.growtech.api.validators.UserRoleValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserRoleValidator.class)
public @interface IsUserRole {
  String message() default "Invalid user role"; // Error message
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
