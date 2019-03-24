package com.diamante.orderingsystem.utils;

import com.diamante.orderingsystem.controller.EntityValidationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ExceptionUtils {

    public static void createErrorMessageAndThrowEntityValidationException(BindingResult result) {
        FieldError fieldError = result.getFieldErrors().get(0);
        String errorMessage = "The field " + fieldError.getField() +
                " with value " + fieldError.getRejectedValue() + " does not meet requirements. ";
        throw new EntityValidationException(errorMessage + fieldError.getDefaultMessage());
    }

}
