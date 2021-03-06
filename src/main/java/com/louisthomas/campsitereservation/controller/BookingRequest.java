package com.louisthomas.campsitereservation.controller;


import com.louisthomas.campsitereservation.common.validation.ValidStartDate;
import com.louisthomas.campsitereservation.common.validation.ValidStayPeriod;
import com.louisthomas.campsitereservation.common.validation.ValidDateRange;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ValidStayPeriod
@ValidDateRange
public class BookingRequest {

    @Email
    private String email;

    @NotEmpty
    @Size(max = 200)
    private String fullName;

    @NotNull
    @Future(message = "{booking.startDate.mustBeInTheFuture}")
    @ValidStartDate
    private LocalDate startDate;

    @NotNull
    @Future(message = "{booking.endDate.mustBeInTheFuture}")
    private LocalDate endDate;
}
