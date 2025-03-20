package com.example.demo.service;


import com.example.demo.Exception.AddressNotFoundException;
import com.example.demo.Exception.NotAcceptedUserException;
import com.example.demo.Exception.UserNotFoundException;
import com.example.demo.domain.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), "form exceptionHandler"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<Object> handleAddressNotFoundException(AddressNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(e.getMessage(), "form exceptionHandler"));
    }


    @ExceptionHandler(NotAcceptedUserException.class)
    public ResponseEntity<Object> handleNotAcceptedUserException(NotAcceptedUserException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), "form exceptionHandler"), HttpStatus.FORBIDDEN);
    }
}
