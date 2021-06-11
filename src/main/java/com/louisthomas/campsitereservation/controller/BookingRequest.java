package com.louisthomas.campsitereservation.controller;


import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
//todo: Add 3 days max validation and startDate before enddate validation
public class BookingRequest {
    @Email
    private String email;

    @NotEmpty
    @Size(max = 50)
    private String fullName;

    @NotNull
    @Future(message = "Booking start date must be in the future date")
    private LocalDate startDate;

    @NotNull
    @Future(message = "Booking end date must be in the future date")
    private LocalDate endDate;
}
