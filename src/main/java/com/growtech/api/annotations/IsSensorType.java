package com.growtech.api.annotations;

import com.growtech.api.validators.SensorTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SensorTypeValidator.class)
public @interface IsSensorType {
  String message() default "Invalid sensor type"; // Error message
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
