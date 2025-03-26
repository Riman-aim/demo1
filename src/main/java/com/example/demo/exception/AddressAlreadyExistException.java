package com.example.demo.exception;

public class AddressAlreadyExistException extends RuntimeException {
    public AddressAlreadyExistException(String message) {
        super(message);
    }
}
