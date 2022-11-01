package com.example.spring_hex_practive.controller;

import com.example.spring_hex_practive.controller.dto.response.GetTargetTrainResponse;
import com.example.spring_hex_practive.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@RestController
@RequestMapping("/train")
@Validated
public class TrainController {
    @Autowired
    private TrainService trainService;
    @GetMapping("/{trainNo}/stops")
    public GetTargetTrainResponse getTargetTrain(@PathVariable @Min(value =0,message = "車次必須為正整數") int trainNo){
        GetTargetTrainResponse response = trainService.getTargetTrain(trainNo);
        return response;
    }
}
