package com.example.spring_hex_practive.controller.dto.response;

import com.example.spring_hex_practive.exception.DataNotFoundException;
import com.example.spring_hex_practive.exception.CheckTrainException;
import lombok.*;
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
public class ErrorResponse {
    private String error;
    private List<Map<String, String>> fieldError;

    public ErrorResponse(CheckTrainException e) {
        this.error = "Validate Failed";
        this.fieldError = e.getErrorList();
    }

    public ErrorResponse(NumberFormatException e) {
        this.error = "Validate Failed";
        this.fieldError = new ArrayList<>();
        Map<String, String> map = new HashMap<>();

        map.put("code", "Min");

        map.put("message", "車次必須為正整數");

        map.put("field", "trainNo");

        fieldError.add(map);
    }


    public ErrorResponse(ConstraintViolationException e) {
        this.error = "Validate Failed";

        this.fieldError = new ArrayList<>();

        e.getConstraintViolations().stream().forEach(c -> {

            String fieldName = "";
            //LeafNode //nodeList
            for (Path.Node node : c.getPropertyPath()) {
                fieldName = node.getName();
            }

            Map<String, String> map = new HashMap<>();

            String code = c.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
            if (!"NotEmpty".equals(code)) {
                map.put("code", code);
            }
            map.put("message", c.getMessage());
            map.put("field", fieldName);

            fieldError.add(map);
        });
    }

    public ErrorResponse(MethodArgumentNotValidException e) {
        this.error = "Validate Failed";
        this.fieldError = new ArrayList<>();

        e.getBindingResult().getFieldErrors().forEach(m -> {
            Map<String, String> fieldMap = new HashMap<>();

            // 欄位名稱
            fieldMap.put("fields", m.getField());

            // 錯誤類型，例 : NotNull 或是 NotBlank
            fieldMap.put("code", m.getCode());

            // 錯誤訊息，例 : 年齡不可為空
            fieldMap.put("message ", m.getDefaultMessage());

            fieldError.add(fieldMap);
        });
    }
}
