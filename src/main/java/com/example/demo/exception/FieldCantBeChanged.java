package com.example.demo.exception;

public class FieldCantBeChanged extends RuntimeException {
    public FieldCantBeChanged(String message) {
        super(message);
    }
}
