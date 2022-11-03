package com.example.spring_hex_practive.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class DataNotFoundException extends Exception {
    private String message;

    public DataNotFoundException(String message) {
        this.message = message;
    }
}
