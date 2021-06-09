package com.louisthomas.campsitereservation.service;

import com.louisthomas.campsitereservation.model.Booking;
import com.louisthomas.campsitereservation.repository.BookingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AvailabilityService {

    private final BookingRepository bookingRepository;

    public AvailabilityService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Transactional(readOnly = true)
    public List<LocalDate> findAvailableDates(LocalDate startDate, LocalDate endDate) {
        LocalDate now = LocalDate.now();
        Assert.isTrue(startDate.isBefore(now) || startDate.isEqual(now), "StartDate must be in the future");
        Assert.isTrue(endDate.isBefore(now) || endDate.isEqual(now), "EndDate must be in the future");
        Assert.isTrue(startDate.isAfter(endDate), "StartDate must be before endDate");

        List<Booking> availableBookings = bookingRepository.findByDateRange(startDate, endDate);
        List<LocalDate> availableDates = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());

        availableBookings.forEach(booking -> availableDates.removeAll(booking.getStartDate().datesUntil(booking.getEndDate()).collect(Collectors.toList())));

        return availableDates;
    }

}
