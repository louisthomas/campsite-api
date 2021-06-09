package com.louisthomas.campsitereservation.controller;

import com.louisthomas.campsitereservation.service.AvailabilityService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AvailabilityController.class)
public class AvailabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AvailabilityService availabilityService;

    @Test
    public void whenvalidInput_thenReturns200() throws Exception {

        given(availabilityService.findAvailableDates(any(LocalDate.class), any(LocalDate.class)))
                .willReturn(Arrays.asList(LocalDate.now()));

        mockMvc.perform(get("/api/v1/availabilities")
                .param("startDate", "2")
                .param("endDate", "2020-10-11"))
                .andDo(print())
//                .andExpect(status().isOk())
                .andExpect(content().string("test"));
    }

}