package com.growtech.api.annotations;

import com.growtech.api.validators.SensorUnitValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SensorUnitValidator.class)
public @interface IsSensorUnit {
  String message() default "Invalid sensor unit"; // Error message
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
