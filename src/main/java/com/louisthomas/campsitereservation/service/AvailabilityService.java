package com.louisthomas.campsitereservation.service;

import com.louisthomas.campsitereservation.model.Booking;
import com.louisthomas.campsitereservation.repository.BookingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AvailabilityService {

    private final BookingRepository bookingRepository;

    public AvailabilityService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Transactional(readOnly = true)
    public Set<LocalDate> findAvailableDates(LocalDate startDate, LocalDate endDate) {
        var now = LocalDate.now();
        log.debug("Now {}", now);
        Assert.isTrue(startDate.isAfter(now) || startDate.isEqual(now), "StartDate must be in the future");
        Assert.isTrue(endDate.isAfter(now) || endDate.isEqual(now), "EndDate must be in the future");
        Assert.isTrue(startDate.isBefore(endDate), "StartDate must be before endDate");

        List<Booking> availableBookings = bookingRepository.findByDateRange(startDate, endDate);
        log.debug("Find all bookings: {}", availableBookings);
        Set<LocalDate> availableDates = startDate.datesUntil(endDate.plusDays(1)).distinct()
                .collect(Collectors.toCollection(TreeSet::new));
        availableBookings.forEach(booking -> availableDates
                .removeAll(booking.getStartDate().datesUntil(booking.getEndDate()).collect(Collectors.toList())));
        log.info("Available dates: {}", availableDates);
        return availableDates;
    }
}