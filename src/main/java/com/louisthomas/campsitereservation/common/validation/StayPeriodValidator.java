package com.louisthomas.campsitereservation.common.validation;

import com.louisthomas.campsitereservation.controller.BookingRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Period;
import java.util.Objects;

@Slf4j
public class StayPeriodValidator implements ConstraintValidator<ValidStayPeriod, BookingRequest> {

    @Value("${booking.periodStay.max:3}")
    private int maxPeriodStay;

    @Override
    public boolean isValid(BookingRequest booking, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(booking)) {
            return false;
        }
        log.debug("Max period stay value {}", maxPeriodStay);
        int periodStayDays = Period.between(booking.getStartDate(), booking.getEndDate()).getDays();
        log.debug("Start date {}, end date {}, duration {}", booking.getStartDate(), booking.getEndDate(),
                  periodStayDays);
        return periodStayDays <= maxPeriodStay;
    }
}
