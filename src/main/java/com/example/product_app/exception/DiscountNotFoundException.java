package com.example.product_app.exception;

public class DiscountNotFoundException extends RuntimeException{

    public DiscountNotFoundException(String message) {
        super(message);
    }
}
