package com.example.product_app.exception;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public class ExceptionResponse {

    private int statusCode;
    private String message;
    private Instant timestamp = Instant.now();

    public ExceptionResponse() {
    }

    public ExceptionResponse(HttpStatus statusCode, String message) {
        this.statusCode = statusCode.value();
        this.message = message;
    }


    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}

