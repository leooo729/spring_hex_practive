package com.example.spring_hex_practive.controller;

import com.example.spring_hex_practive.controller.dto.response.GetTargetTrainResponse;
import com.example.spring_hex_practive.service.TrainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/train")
public class TrainController {
    @Autowired
    private TrainService trainService;
    @GetMapping("/{trainNo}/stops")
    public GetTargetTrainResponse getTargetTrain(@PathVariable int trainNo){
        GetTargetTrainResponse response = trainService.getTargetTrain(trainNo);
        return response;
    }
}
