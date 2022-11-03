package com.example.spring_hex_practive.advice;

import com.example.spring_hex_practive.controller.dto.response.ErrorResponse;
import com.example.spring_hex_practive.exception.DataNotFoundException;
import com.example.spring_hex_practive.exception.CheckTrainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ApplicationExceptionHandler {
    //Exception
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handler(Exception e) {
//
//        ErrorResponse error = new ErrorResponse(e);
//
//        return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
//    }

//    MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handler(MethodArgumentNotValidException e) {

        ErrorResponse error = new ErrorResponse(e);

        return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
    }

    //ConstraintViolationException
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handler(ConstraintViolationException e) {

        ErrorResponse error = new ErrorResponse(e);

        return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<Map> handler(DataNotFoundException e) {

//        ErrorResponse error = new ErrorResponse(e);
        Map<String,String>errorMessage=new HashMap<>();
        errorMessage.put("error",e.getMessage());

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(CheckTrainException.class)
    public ResponseEntity<ErrorResponse> handler(CheckTrainException e) {

        ErrorResponse error = new ErrorResponse(e);

        return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
    }

}
