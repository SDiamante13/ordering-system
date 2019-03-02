package com.diamante.orderingsystem.controller.customer;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CustomerNotFoundException extends RuntimeException {
    String message;

    public CustomerNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
