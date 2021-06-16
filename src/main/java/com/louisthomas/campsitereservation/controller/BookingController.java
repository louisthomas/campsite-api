package com.louisthomas.campsitereservation.controller;

import com.louisthomas.campsitereservation.model.Booking;
import com.louisthomas.campsitereservation.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestBody @Valid BookingRequest bookingRequest) {
        log.debug("Create booking {}", bookingRequest);
        BookingDto bookingDto = bookingService.createBooking(bookingRequest);
        return ResponseEntity.created(URI.create("/api/v1/bookings/" + bookingDto.getId())).body(bookingDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable("id") UUID id) {
        log.debug("Get booking for id {}", id);
        Booking booking = bookingService.findBookingById(id);
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<BookingDto> updateBooking(@PathVariable("id") UUID id, @RequestBody @Valid BookingRequest bookingRequest) {
        log.debug("Update booking with id {} and payload: {}", id, bookingRequest);
        BookingDto bookingDto = bookingService.updateBooking(id, bookingRequest);
        return new ResponseEntity<>(bookingDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable("id") UUID id) {
        log.debug("Cancel booking for id {}", id);
        bookingService.cancelBooking(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}