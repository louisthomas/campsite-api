package com.louisthomas.campsitereservation.graphql;

import com.louisthomas.campsitereservation.controller.BookingDto;
import com.louisthomas.campsitereservation.controller.BookingRequest;
import com.louisthomas.campsitereservation.service.AvailabilityService;
import com.louisthomas.campsitereservation.service.BookingService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@DgsComponent
@Slf4j
public class BookingDataFetcher {

    private final BookingService bookingService;
    private final AvailabilityService availabilityService;

    public BookingDataFetcher(BookingService bookingService, AvailabilityService availabilityService) {
        this.bookingService = bookingService;
        this.availabilityService = availabilityService;
    }

    @DgsData(parentType = "Query", field = "availability")
    public Set<LocalDate> availability(@InputArgument("startDate") LocalDate startDate,
                                       @InputArgument("endDate") LocalDate endDate) {
        return availabilityService.findAvailableDates(startDate, endDate);
    }

    @DgsData(parentType = "Query", field = "booking")
    public List<BookingDto> bookings(@InputArgument("id") String id) {
        if (StringUtils.hasText(id)) {
            return Arrays.asList(bookingService.findBookingById(UUID.fromString(id)));
        }
        return bookingService.findAll();
    }

    @DgsMutation()
    public BookingDto addBooking(@InputArgument(value = "booking") BookingRequest bookingRequest) {
        log.info("Booking DATA {}", bookingRequest);
        return bookingService.createBooking(bookingRequest);
    }

    @DgsMutation()
    public BookingDto updateBooking(@InputArgument(value = "id") String id,
                                    @InputArgument(value = "booking") BookingRequest bookingRequest) {
        log.info("Booking id {} and DATA {}", id, bookingRequest);
        return bookingService.updateBooking(UUID.fromString(id), bookingRequest);
    }

    @DgsMutation()
    public boolean removeBooking(@InputArgument(value = "id") String id) {
        log.info("Booking id to remove: {}", id);
        bookingService.cancelBooking(UUID.fromString(id));
        return true;
    }

}
