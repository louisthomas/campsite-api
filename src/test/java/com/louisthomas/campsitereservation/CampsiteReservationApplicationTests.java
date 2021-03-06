package com.louisthomas.campsitereservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.louisthomas.campsitereservation.controller.BookingRequest;
import com.louisthomas.campsitereservation.model.Booking;
import com.louisthomas.campsitereservation.repository.BookingRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class CampsiteReservationApplicationTests extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void init() {
        this.bookingRepository.deleteAll();
    }

    @AfterEach
    public void cleanUp() {
        this.bookingRepository.deleteAll();
    }

    @Test
    void shouldReturnSuccessWhenUpdateBooking() throws Exception {

        String expectedEmail = "expected@test.com";
        String expectedFullName = "Expected test";

        //given
        Booking booking = bookingRepository
                .save(new Booking(UUID.randomUUID(), "test@test.com", "Test test", LocalDate.now(), LocalDate.now(),
                                  Instant.now(), Instant.now()));
        UUID expectedId = booking.getId();
        log.info("Booking id: {}", expectedId);

        BookingRequest bookingRequest = new BookingRequest(expectedEmail, expectedFullName,
                                                           LocalDate.now().plusDays(2), LocalDate.now().plusDays(4));

        mockMvc.perform(
                put("/api/v1/bookings/{id}", expectedId).content(objectMapper.writeValueAsString(bookingRequest))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedId.toString()))
                .andExpect(jsonPath("$.email").value(expectedEmail))
                .andExpect(jsonPath("$.fullName").value(expectedFullName));
    }

    @Test
    void shouldReturn404NotWhenDeleteNotfoundBooking() throws Exception {
        mockMvc.perform(
                delete("/api/v1/bookings/{id}", UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Can't find booking with id")))
                .andExpect(jsonPath("$.status", is("NOT_FOUND")));
    }

    @Test
    void shouldReturn204NotWhenDeleteBooking() throws Exception {
        //given
        Booking booking = bookingRepository
                .save(new Booking(UUID.randomUUID(), "test@test.com", "Test test", LocalDate.now(), LocalDate.now(),
                                  Instant.now(), Instant.now()));
        UUID expectedId = booking.getId();
        log.info("Booking id: {}", expectedId);

        mockMvc.perform(
                delete("/api/v1/bookings/{id}", expectedId))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(
                get("/api/v1/bookings/{id}", expectedId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("concurrency post to test booking overlapping constraint")
    void concurrencyTest() throws Exception {
        LocalDate startDate = LocalDate.now().plusDays(2);
        LocalDate endDate = LocalDate.now().plusDays(5);

        ExecutorService executorService = Executors
                .newFixedThreadPool(2, new ThreadFactoryBuilder().setNameFormat("test-%d").build());
        BookingRequest firstBookingRequest = new BookingRequest("test@test.com", "Test test", startDate, endDate);
        BookingRequest secondBookingRequest = new BookingRequest("test2@test2.com", "Test2 test2", startDate, endDate);

        //request multiple async call on different threads Java8
        CompletableFuture<MvcResult> firstBooking = CompletableFuture
                .supplyAsync(() -> createNewBooking(firstBookingRequest), executorService);
        CompletableFuture<MvcResult> secondBooking = CompletableFuture
                .supplyAsync(() -> createNewBooking(secondBookingRequest), executorService);

        // wait until they are all done
        CompletableFuture.allOf(firstBooking, secondBooking).join();
        MockHttpServletResponse firstResponse = firstBooking.get().getResponse();
        MockHttpServletResponse secondresponse = secondBooking.get().getResponse();

        //We don't know which thread is the fastest
        if (firstResponse.getStatus() == HttpStatus.CREATED.value()) {
            assertThat(secondresponse.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        } else {
            assertThat(firstResponse.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
            assertThat(secondresponse.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        }
    }

    @SneakyThrows
    private MvcResult createNewBooking(BookingRequest request) {
        log.info("Request {}", request);
        return mockMvc.perform(post("/api/v1/bookings").content(objectMapper.writeValueAsString(request))
                                       .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andReturn();
    }


}
