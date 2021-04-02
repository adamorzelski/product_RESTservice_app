package com.example.product_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RegisterErrorAdvice {

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse productNotFoundException(ProductNotFoundException ex) {
        return new ExceptionResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(VisitCounterNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse visitCounterNotFoundException(VisitCounterNotFoundException ex) {
        return new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(DiscountNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse discountCounterNotFoundException(DiscountNotFoundException ex) {
        return new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}
