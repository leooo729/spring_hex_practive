package com.example.spring_hex_practive.controller.dto.response;

import com.example.spring_hex_practive.exception.CheckTrainException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckErrorResponse {
    private String error;
    private List<Map<String, String>> checkErrors;
    public CheckErrorResponse(CheckTrainException e) {
        this.error = "Validate Failed";
        this.checkErrors = e.getErrorList();
    }
}
