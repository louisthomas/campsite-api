package com.louisthomas.campsitereservation.controller;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
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
