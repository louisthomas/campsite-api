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
    public ResponseEntity<Booking> createBooking(@RequestBody @Valid BookingRequest bookingRequest) {
        Booking booking = bookingService.createBooking(bookingRequest);
        return ResponseEntity.created(URI.create("/api/v1/bookings/" + booking.getId())).body(booking);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable() UUID id) {
        Booking booking = bookingService.findBookingById(id);
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable("id") UUID id, @RequestBody @Valid BookingRequest bookingRequest) {
        Booking booking = bookingService.updateBooking(id, bookingRequest);
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable("id") UUID id) {
        bookingService.cancelBooking(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}