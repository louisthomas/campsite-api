package com.louisthomas.campsitereservation.controller;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class BookingDto {

    private UUID id;

    private String email;

    private String fullName;

    private LocalDate startDate;

    private LocalDate endDate;
}
