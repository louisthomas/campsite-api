package com.louisthomas.campsitereservation.service;

import com.louisthomas.campsitereservation.common.exception.BookingNotFoundException;
import com.louisthomas.campsitereservation.controller.BookingDto;
import com.louisthomas.campsitereservation.model.Booking;
import com.louisthomas.campsitereservation.repository.BookingRepository;
import com.louisthomas.campsitereservation.controller.BookingRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;

    private ModelMapper modelMapper;

    public BookingService(BookingRepository bookingRepository, ModelMapper modelMapper) {
        this.bookingRepository = bookingRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public BookingDto createBooking(BookingRequest bookingRequest) {
        Booking booking = bookingRepository.save(modelMapper.map(bookingRequest, Booking.class));
        return modelMapper.map(booking, BookingDto.class);
    }

    @Transactional(readOnly = true)
    public BookingDto findBookingById(UUID id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        booking.orElseThrow(() -> new BookingNotFoundException(id));
        return modelMapper.map(booking, BookingDto.class);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public BookingDto updateBooking(UUID id, BookingRequest bookingRequest) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException(id));
        booking.setEmail(bookingRequest.getEmail());
        booking.setFullName(bookingRequest.getFullName());
        booking.setStartDate(bookingRequest.getStartDate());
        booking.setEndDate(bookingRequest.getEndDate());
        return modelMapper.map(bookingRepository.save(booking), BookingDto.class);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void cancelBooking(UUID id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException(id));
        bookingRepository.delete(booking);
        log.info("Deleted booking with id: {}", id  );
    }

    @Transactional(readOnly = true)
    public List<BookingDto> findAll() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream().map(booking -> modelMapper.map(booking, BookingDto.class)).collect(Collectors.toList());
    }
}
