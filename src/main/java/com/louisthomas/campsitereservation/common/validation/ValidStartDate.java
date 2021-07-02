package com.louisthomas.campsitereservation.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(value = {ElementType.FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {StartDateValidator.class})
@Documented
public @interface ValidStartDate {
    String message() default "{booking.startDate.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
