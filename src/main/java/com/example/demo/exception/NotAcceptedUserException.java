package com.example.demo.exception;


public class NotAcceptedUserException extends RuntimeException {
    public NotAcceptedUserException(String message) {
        super(message);
    }

}
