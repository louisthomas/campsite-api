package com.louisthomas.campsitereservation.repository;

import com.louisthomas.campsitereservation.AbstractIntegrationTest;
import com.louisthomas.campsitereservation.model.Booking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest
//Tell Spring not to replace the database with an embedded database
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookingRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    public void shouldReturnsAllBookingsByDateRange() {
        bookingRepository.save(new Booking(UUID.randomUUID(), "first.person@test.com", "First Person",
                LocalDate.parse("2021-06-14"), LocalDate.parse("2021-06-16"), Instant.now(), Instant.now()));

        bookingRepository.save(new Booking(UUID.randomUUID(), "second.person@test.com", "Second Person",
                LocalDate.parse("2021-06-17"), LocalDate.parse("2021-06-20"), Instant.now(), Instant.now()));

        List<Booking> bookingList = bookingRepository
                .findByDateRange(LocalDate.parse("2021-06-14"), LocalDate.parse("2021-06-17"));
        assertThat(bookingList.size()).isEqualTo(2);
    }

    @Test
    public void shouldReturnsDataIntegrityViolationExceptionWhenDateRangeOverlap() {
        bookingRepository.save(new Booking(UUID.randomUUID(), "first.person@test.com", "First Person",
                LocalDate.parse("2021-06-14"), LocalDate.parse("2021-06-16"), Instant.now(), Instant.now()));

        bookingRepository.save(new Booking(UUID.randomUUID(), "second.person@test.com", "Second Person",
                LocalDate.parse("2021-06-15"), LocalDate.parse("2021-06-17"), Instant.now(), Instant.now()));

        assertThrows(DataIntegrityViolationException.class, () -> bookingRepository
                .findByDateRange(LocalDate.parse("2021-06" + "-14"), LocalDate.parse("2021-06-17")));

    }

}