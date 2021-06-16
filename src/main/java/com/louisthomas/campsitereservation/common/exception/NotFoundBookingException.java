package com.louisthomas.campsitereservation.common.exception;

import java.util.UUID;

public class NotFoundBookingException extends RuntimeException {

    public NotFoundBookingException(UUID id) {
        super(String.format("Can't find booking with id %s", id.toString()));
    }

    public NotFoundBookingException(String message) {
        super(message);
    }
}
