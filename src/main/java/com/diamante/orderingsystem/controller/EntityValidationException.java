package com.diamante.orderingsystem.controller;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

    @Data
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public class EntityValidationException extends RuntimeException {
        private final String message;

        public EntityValidationException(String message) {
            super(message);
            this.message = message;
        }
    }
