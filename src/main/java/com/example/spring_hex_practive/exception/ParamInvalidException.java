package com.example.spring_hex_practive.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ParamInvalidException extends Exception {

    private List<String> errMessages;

    public ParamInvalidException(List<String> errMessages) {
        this.errMessages = errMessages;
    }
}
