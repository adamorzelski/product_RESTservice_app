package com.example.product_app.exception;

public class VisitCounterNotFoundException extends RuntimeException{

    public VisitCounterNotFoundException(String message) {
        super(message);
    }
}
