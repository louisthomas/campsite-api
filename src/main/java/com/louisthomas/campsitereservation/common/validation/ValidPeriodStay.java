package com.louisthomas.campsitereservation.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PeriodStayValidator.class})
@Documented
public @interface ValidPeriodStay {

    String message() default "Booking must be equals or less than three days";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
