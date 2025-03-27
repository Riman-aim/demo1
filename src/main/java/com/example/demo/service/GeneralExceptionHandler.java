package com.example.demo.service;


import com.example.demo.exception.*;
import com.example.demo.dto.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), "form exceptionHandler"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleAddressNotFoundException(AddressNotFoundException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), "form exceptionHandler"), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(NotAcceptedUserException.class)
    public ResponseEntity<ExceptionResponse> handleNotAcceptedUserException(NotAcceptedUserException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), "form exceptionHandler"), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(FieldCantBeChanged.class)
    public ResponseEntity<ExceptionResponse> handleFieldCantBeChanged(FieldCantBeChanged e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), "form exceptionHandler"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicateUsernameException(DuplicateUsernameException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), "form exceptionHandler"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicatePhoneNumberException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicatePhoneNumberException(DuplicatePhoneNumberException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), "form exceptionHandler"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AddressNotInitialized.class)
    public ResponseEntity<ExceptionResponse> handleAddressNotInitialized(AddressNotInitialized e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), "form exceptionHandler"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AddressAlreadyExistException.class)
    public ResponseEntity<ExceptionResponse> handleAddressAlreadyExistException(AddressAlreadyExistException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), "form exceptionHandler"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HashMap<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        HashMap<String,String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
