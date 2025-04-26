package com.proyecto.apisensores.annotations;

import com.proyecto.apisensores.validators.SensorTypeValidator;
import com.proyecto.apisensores.validators.SensorUnitValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SensorUnitValidator.class)
public @interface IsSensorUnit {
  String fieldName() default "sensor_unit"; // Field name
  Class<? extends Payload>[] payload() default {};
}
