package com.custempmanag.librarymanagmentsystem.exception;

import com.custempmanag.librarymanagmentsystem.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        MessageResponse response = new MessageResponse(
                HttpStatus.BAD_REQUEST.toString(), // status
                "Validation failed", // message
                errors // data
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomExceptions(CustomException ex) {

        MessageResponse response = new MessageResponse(
                HttpStatus.BAD_REQUEST.toString(), // status
                ex.getMessage(), // message
                null // data
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex) {
        MessageResponse response = new MessageResponse(
                HttpStatus.NOT_FOUND.toString(),  // status
                ex.getMessage(),  // message (custom message from exception)
                null  // data
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);  // 404 Not Found
    }
}
