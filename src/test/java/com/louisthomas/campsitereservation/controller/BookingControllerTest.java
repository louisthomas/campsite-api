package com.louisthomas.campsitereservation.controller;

import com.louisthomas.campsitereservation.service.BookingService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;


    @Test
    public void shouldReturn201WhenCreateNewBooking() {
        given(bookingService.createBooking(any(BookingRequest.class))).willReturn(new BookingDto());

        //mockMvc.perform(post());
    }
}