package com.louisthomas.campsitereservation.service;

import com.louisthomas.campsitereservation.model.Booking;
import com.louisthomas.campsitereservation.repository.BookingRepository;
import com.louisthomas.campsitereservation.controller.BookingRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class BookingService {

    private BookingRepository bookingRepository;

    private ModelMapper modelMapper;

    public BookingService(BookingRepository bookingRepository, ModelMapper modelMapper) {
        this.bookingRepository = bookingRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Booking createBooking(BookingRequest booking) {
        //todo: Date overlap query
        //Return error with dates
        return bookingRepository.save(modelMapper.map(booking, Booking.class));
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public Booking findBookingById(UUID id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        //todo: send custom exception throw new BookingNotFoundException(String.format("booking not found with id=%d",id)
        return booking.orElseThrow(RuntimeException::new);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Booking updateBooking(UUID id, BookingRequest booking) {
        return null;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void cancelBooking(UUID id) {
        bookingRepository.deleteById(id);
        log.info("Deleted booking with id: {}", id  );
    }
}
