package com.diamante.orderingsystem.controller.customer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<?> customerNotFoundException(CustomerNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timeStamp(LocalDateTime.now())
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }


}
