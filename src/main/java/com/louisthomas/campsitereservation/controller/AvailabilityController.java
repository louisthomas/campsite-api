package com.louisthomas.campsitereservation.controller;

import com.louisthomas.campsitereservation.service.AvailabilityService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/api/v1/availabilities")
public class AvailabilityController {

    private int defaultSearchEndDate;

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService, @Value("${test:30}") int defaultSearchEndDate) {
        this.availabilityService = availabilityService;
        this.defaultSearchEndDate = defaultSearchEndDate;
    }

    @GetMapping
    public ResponseEntity<List<LocalDate>> getReservation(
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @FutureOrPresent LocalDate startDate,
            @RequestParam(required = false)  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Future Optional<LocalDate> endDate) {
        return ResponseEntity.ok(availabilityService.findAvailableDates(startDate, endDate.orElseGet(() -> startDate.plusDays(defaultSearchEndDate))));
    }
}
