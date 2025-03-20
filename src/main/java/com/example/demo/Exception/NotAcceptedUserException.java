package com.example.demo.Exception;

public class NotAcceptedUserException extends RuntimeException {
    public NotAcceptedUserException(String message) {
        super(message);
    }

}
