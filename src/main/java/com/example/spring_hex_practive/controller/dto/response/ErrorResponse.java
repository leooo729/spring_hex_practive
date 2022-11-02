package com.example.spring_hex_practive.controller.dto.response;

import com.example.spring_hex_practive.exception.DataNotFoundException;
import com.example.spring_hex_practive.exception.TrainNoException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class ErrorResponse {
    private String error;
    private List<Map<String, String>> fieldError;
    public ErrorResponse(TrainNoException e){
        this.error = "Validate Failed";
        this.fieldError = new ArrayList<>();

        Map<String, String> map = new HashMap<>();

fieldError.add(map);
    }


    public ErrorResponse(ConstraintViolationException e) {
        this.error = "Validate Failed";

        this.fieldError = new ArrayList<>();

        // 因為未通過基礎檢核的欄位可能不只一個
        // 所以需要呼叫 e.getConstraintViolations() 取得不符合基礎檢核的欄位
        // 再放入 fieldError 中

        e.getConstraintViolations().stream().forEach(c -> {

            String fieldName = "";
            //LeafNode //nodeList
            for (Path.Node node : c.getPropertyPath()) {
                fieldName = node.getName();
            }

            Map<String, String> map = new HashMap<>();
            // 錯誤類型
            map.put("code", c.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());
            // 錯誤訊息
            map.put("message", c.getMessage());
            // 欄位名稱
            map.put("field", fieldName);

            fieldError.add(map);
        });
    }

    public ErrorResponse(DataNotFoundException e) {
        this.error = e.getMessage();
    }

    public ErrorResponse(MethodArgumentNotValidException e) {
        this.error="Validate Failed";
        this.fieldError = new ArrayList<>();

        // 因為未通過基礎檢核的欄位可能不只一個
        // 所以需要呼叫 e.getBindingResult().getFieldErrors() 取得不符合基礎檢核的欄位
        // 再放入 fieldError 中

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
