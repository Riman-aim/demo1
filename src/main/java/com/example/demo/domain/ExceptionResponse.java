package com.example.demo.domain;

import java.time.LocalDateTime;

public class ExceptionResponse {
    private String message;
    private String details;
    private LocalDateTime timestamp;

    public ExceptionResponse(String message, String details) {
        this.message = message;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
