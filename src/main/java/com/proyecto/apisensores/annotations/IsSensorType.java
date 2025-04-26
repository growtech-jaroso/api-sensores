package com.proyecto.apisensores.annotations;

import com.proyecto.apisensores.validators.SensorTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SensorTypeValidator.class)
public @interface IsSensorType {
  String message() default "Invalid sensor type"; // Error message
  // Get the field name
  Class<? extends Payload>[] payload() default {};
}
