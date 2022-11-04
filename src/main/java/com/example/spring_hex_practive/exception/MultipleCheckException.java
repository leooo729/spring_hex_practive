package com.example.spring_hex_practive.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
@Data
@NoArgsConstructor
public class MultipleCheckException extends Exception {
    private List<Map<String,String>> errorList;
    public MultipleCheckException(List<Map<String,String>> errorList) {
        this.errorList = errorList;
    }

}
