package com.example.spring_hex_practive.controller;

import com.example.spring_hex_practive.controller.dto.request.CreateTrainRequest;
import com.example.spring_hex_practive.controller.dto.response.*;
//import com.example.spring_hex_practive.exception.DataNotFoundException;
import com.example.spring_hex_practive.exception.DataNotFoundException;
import com.example.spring_hex_practive.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("/train")
@Validated
public class TrainController {
    @Autowired
    private TrainService trainService;

    @GetMapping("/{trainNo}/stops")
    public GetTargetTrainResponse getTargetTrain(@PathVariable @Min(value = 0, message = "車次必須為正整數") int trainNo) throws DataNotFoundException {
        GetTargetTrainResponse response = trainService.getTargetTrain(trainNo);
        return response;
    }

    @GetMapping
    public List<TrainInfo> getTrainByStop(@RequestParam @NotEmpty(message = "Required String parameter 'via' is not present") String via) {
        List<TrainInfo> response = trainService.getTrainByStop(via);
        return response;
    }

    @PostMapping
    public CreateTrainResponse createTrainInfo(@RequestBody @Valid CreateTrainRequest request) {
        CreateTrainResponse response = trainService.CreateTrainInfo(request);
        return response;
    }
}
