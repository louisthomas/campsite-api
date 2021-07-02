package com.louisthomas.campsitereservation.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {StayPeriodValidator.class})
@Documented
public @interface ValidStayPeriod {

    String message() default "{booking.stayPeriod.invalid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
