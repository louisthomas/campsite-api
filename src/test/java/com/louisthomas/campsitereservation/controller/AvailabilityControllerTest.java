package com.louisthomas.campsitereservation.controller;

import com.louisthomas.campsitereservation.service.AvailabilityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AvailabilityController.class)
class AvailabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AvailabilityService availabilityService;

    @Test
    void shouldReturns200WhenValidationDateRange() throws Exception {
        given(availabilityService.findAvailableDates(any(LocalDate.class), any(LocalDate.class)))
                .willReturn(Set.of(LocalDate.now()));

        mockMvc.perform(get("/api/v1/availabilities").param("startDate", LocalDate.now().plusDays(1).toString())
                .param("endDate", LocalDate.now().plusDays(3).toString())).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void shouldReturns400WhenThrowIllegalArgumentException() throws Exception {
        given(availabilityService.findAvailableDates(any(LocalDate.class), any(LocalDate.class)))
                .willThrow(new IllegalArgumentException("StartDate must be in the future"));

        mockMvc.perform(get("/api/v1/availabilities").param("startDate", LocalDate.now().minusDays(1).toString())
                .param("endDate", LocalDate.now().plusDays(3).toString())).andDo(print())
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("StartDate must be in the future"));
    }
}