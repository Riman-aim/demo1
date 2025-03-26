package com.example.demo.exception;

public class AddressNotInitialized extends RuntimeException {
    public AddressNotInitialized(String message) {
        super(message);
    }
}
