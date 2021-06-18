package com.louisthomas.campsitereservation.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class BookingControllerAdvice {

    public static final String POSTGRESQL_EXCLUSION_VIOLATION_ERROR_CODE = "23P01";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.add(createErrorMessage(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        for(ObjectError objectError: ex.getBindingResult().getAllErrors()){
            errors.add(createErrorMessage(objectError.getObjectName(),objectError.getDefaultMessage()));
        }
        var apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintValidationException(ConstraintViolationException ex) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(createErrorMessage(violation.getRootBeanClass().getName(), violation.getMessage()));
        }
        var apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<Object> handlePSQLException(PSQLException ex){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        if(ex.getSQLState().equals(POSTGRESQL_EXCLUSION_VIOLATION_ERROR_CODE)){
            httpStatus = HttpStatus.CONFLICT;
        }
        var apiError = new ApiError(httpStatus, ex.getMessage(), String.format("PostgreSQL error code %s",
                ex.getSQLState()));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        var apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(BookingNotFoundException.class)
    public  ResponseEntity<Object> handleBookingNotFoundException(BookingNotFoundException ex){
        var apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }


    private String createErrorMessage(String fieldName, String message) {
        return String.format("Field: %s, message: %s", fieldName, message);

    }
}
