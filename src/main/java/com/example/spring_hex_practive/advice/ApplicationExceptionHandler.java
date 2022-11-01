package com.example.spring_hex_practive.advice;

import com.example.spring_hex_practive.controller.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ApplicationExceptionHandler {
//    //Exception
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handler(Exception e) {
//
//        ErrorResponse error = new ErrorResponse(e);
//
//        return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
//    }
//
//    //MethodArgumentNotValidException
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> handler(MethodArgumentNotValidException e) {
//
//        ErrorResponse error = new ErrorResponse(e);
//
//        return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
//    }

    //ConstraintViolationException
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handler(ConstraintViolationException e) {

        ErrorResponse error = new ErrorResponse(e);

        return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
    }

}
