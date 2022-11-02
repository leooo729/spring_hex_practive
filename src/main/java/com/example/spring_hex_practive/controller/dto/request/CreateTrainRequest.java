package com.example.spring_hex_practive.controller.dto.request;

import com.example.spring_hex_practive.controller.dto.response.Stops;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTrainRequest {
    @NotNull(message = "車次不可為空")
    private Integer train_no;
    @NotEmpty(message = "車種不可為空")
    private String train_kind;
    @NotEmpty(message = "停靠站不可為空")
    private List<Stops> stops;

}
