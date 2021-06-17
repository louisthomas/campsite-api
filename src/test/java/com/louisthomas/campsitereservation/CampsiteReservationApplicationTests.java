package com.louisthomas.campsitereservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.louisthomas.campsitereservation.controller.BookingRequest;
import com.louisthomas.campsitereservation.repository.BookingRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.google.common.util.concurrent.ThreadFactoryBuilder;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

    @BeforeEach
    public void init() {
        this.bookingRepository.deleteAll();
    }

    @AfterEach
    public void cleanUp() {
        this.bookingRepository.deleteAll();
    }

    @Test
    void conccurrencyTest() throws Exception {
        LocalDate startDate = LocalDate.now().plusDays(2);
        LocalDate endDate = LocalDate.now().plusDays(5);

        ExecutorService executorService = Executors.newFixedThreadPool(2, new ThreadFactoryBuilder().setNameFormat(
                "test-%d").build());
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
        if(firstResponse.getStatus() == HttpStatus.CREATED.value()){
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
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
    }


}
