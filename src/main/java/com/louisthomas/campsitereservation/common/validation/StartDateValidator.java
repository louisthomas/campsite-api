package com.louisthomas.campsitereservation.common.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.Objects;

@Slf4j
public class StartDateValidator implements ConstraintValidator<ValidStartDate, LocalDate> {
    @Value("${booking.days.min:1}")
    private int minDays;
    @Value("${booking.days.max:30}")
    private int maxDays;

    @Override
    public boolean isValid(LocalDate startDate, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(startDate)) {
            return false;
        }
        LocalDate now = LocalDate.now();
        log.debug("Min day(s) {}", minDays);
        log.debug("Max days {}", maxDays);
        log.debug("Start date: {}, actual time: {}", startDate, now);
        return startDate.isAfter(now.plusDays(minDays)) && startDate.isBefore(now.plusDays(maxDays));
    }
}