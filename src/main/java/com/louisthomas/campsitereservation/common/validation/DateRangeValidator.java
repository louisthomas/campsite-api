package com.louisthomas.campsitereservation.common.validation;

import com.louisthomas.campsitereservation.controller.BookingRequest;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

@Slf4j
public class DateRangeValidator implements ConstraintValidator<ValidDateRange, BookingRequest> {

    @Override
    public boolean isValid(BookingRequest booking, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(booking)) {
            return false;
        }
        log.debug("Start date: {}, endDate: {}", booking.getStartDate(), booking.getEndDate());
        return booking.getStartDate().isBefore(booking.getEndDate());
    }
}
