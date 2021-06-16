package com.louisthomas.campsitereservation.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(value = {TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {DateRangeValidator.class})
@Documented
public @interface ValidDateRange {
    String message() default "Booking start date must be before end date";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
