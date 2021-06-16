package com.louisthomas.campsitereservation.common.validation;

import com.louisthomas.campsitereservation.controller.BookingRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, BookingRequest> {

    @Override
    public boolean isValid(BookingRequest booking, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(booking)) {
            return false;
        }
        return booking.getStartDate().isBefore(booking.getEndDate());
    }
}
