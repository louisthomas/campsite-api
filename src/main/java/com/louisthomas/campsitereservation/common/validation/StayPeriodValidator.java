package com.louisthomas.campsitereservation.common.validation;

import com.louisthomas.campsitereservation.controller.BookingRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Period;
import java.util.Objects;

public class StayPeriodValidator implements ConstraintValidator<ValidStayPeriod, BookingRequest> {

    private static final int MAX_PERIOD_STAY = 3;

    @Override
    public boolean isValid(BookingRequest booking, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(booking)) {
            return false;
        }
        return Period.between(booking.getStartDate(), booking.getEndDate()).getDays() <= MAX_PERIOD_STAY;
    }
}
