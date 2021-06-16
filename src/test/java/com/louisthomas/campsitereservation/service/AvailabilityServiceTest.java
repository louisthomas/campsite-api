package com.louisthomas.campsitereservation.service;

import com.louisthomas.campsitereservation.model.Booking;
import com.louisthomas.campsitereservation.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AvailabilityServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private AvailabilityService availabilityService;

    @Test
    public void shouldThrowIllegalExceptionWhenStartDateIsInThePast() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> availabilityService
                        .findAvailableDates(LocalDate.parse("2021-06-10"), LocalDate.parse("2021-06-20")));
        assertThat(illegalArgumentException.getMessage()).isEqualTo("StartDate must be in the future");
    }

    @Test
    public void shouldThrowIllegalExceptionWhenEndDateIsInThePast() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> availabilityService
                        .findAvailableDates(LocalDate.now().plusDays(2), LocalDate.parse("2021-06-10")));

        assertThat(illegalArgumentException.getMessage()).isEqualTo("EndDate must be in the future");
    }

    @Test
    public void shouldThrowIllegalExceptionWhenEndDateIsBeforeStartDate() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> availabilityService.findAvailableDates(LocalDate.now().plusDays(4), LocalDate.now().plusDays(3)));

        assertThat(illegalArgumentException.getMessage()).isEqualTo("StartDate must be before endDate");
    }

    @Test
    public void shouldReturnAvailableDateWhenValidDateRange() {
        LocalDate startDate = LocalDate.now().plusDays(2);
        LocalDate endDate = LocalDate.now().plusDays(5);

        when(bookingRepository.findByDateRange(any(LocalDate.class), any(LocalDate.class))).thenReturn(Arrays.asList(
                new Booking(UUID.randomUUID(), "toto.test@gmail.com", "Toto Test", startDate, endDate,
                        Instant.now(), Instant.now())));

        List<LocalDate> availableDates = availabilityService.findAvailableDates(startDate, endDate);
        assertThat(availableDates.size()).isEqualTo(1);
        assertThat(availableDates.get(0)).isEqualTo(endDate);
    }
}
