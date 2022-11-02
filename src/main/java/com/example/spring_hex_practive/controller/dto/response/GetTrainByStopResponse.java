package com.example.spring_hex_practive.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
//===============================不用了
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetTrainByStopResponse {
    private List<TrainInfo> trainInfo;
}
