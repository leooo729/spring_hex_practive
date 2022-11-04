package com.example.spring_hex_practive.controller.dto.response;

import com.example.spring_hex_practive.controller.dto.request.Stops;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetTargetTrainResponse {
    private Integer train_no;
    private String train_kind;
    private List<Stops> stops;
}
