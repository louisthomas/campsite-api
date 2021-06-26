package com.louisthomas.campsitereservation.controller;

import com.louisthomas.campsitereservation.service.AvailabilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@RestController()
@RequestMapping("/api/v1/availabilities")
@Slf4j
public class    AvailabilityController {

    private final int defaultSearchEndDate;

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService,
                                  @Value("${test:30}") int defaultSearchEndDate) {
        this.availabilityService = availabilityService;
        this.defaultSearchEndDate = defaultSearchEndDate;
    }

    @GetMapping
    public ResponseEntity<Set<LocalDate>> getReservation(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate) {
        log.debug("Find availabilities between {} and {}", startDate, endDate);
        return ResponseEntity.ok(availabilityService
                .findAvailableDates(startDate, endDate.orElseGet(() -> startDate.plusDays(defaultSearchEndDate))));
    }
}