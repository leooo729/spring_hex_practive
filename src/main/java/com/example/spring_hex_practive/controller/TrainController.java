package com.example.spring_hex_practive.controller;

import com.example.spring_hex_practive.controller.dto.request.BuyTicketRequest;
import com.example.spring_hex_practive.controller.dto.request.CreateTrainRequest;
import com.example.spring_hex_practive.controller.dto.response.*;
//import com.example.spring_hex_practive.exception.DataNotFoundException;
import com.example.spring_hex_practive.exception.DataNotFoundException;
import com.example.spring_hex_practive.exception.CheckTrainException;
import com.example.spring_hex_practive.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("")
@Validated
public class TrainController {
    @Autowired
    private TrainService trainService;

    @GetMapping("/train/{trainNo}/stops")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GetTargetTrainResponse getTargetTrain(@PathVariable @Min(value = 0, message = "車次必須為正整數") Integer trainNo) throws DataNotFoundException {
        GetTargetTrainResponse response = trainService.getTargetTrain(trainNo);
        return response;
    }

    @GetMapping("/train")
    public List<TrainInfo> getTrainByStop(@RequestParam @NotEmpty(message = "Required String parameter 'via' is not present") String via) {
        List<TrainInfo> response = trainService.getTrainByStop(via);
        return response;
    }

    @PostMapping("/train")
    public ResponseEntity<Map<String,String>> createTrainInfo(@RequestBody @Valid CreateTrainRequest request) throws CheckTrainException {
        Map response = trainService.CreateTrainInfo(request);
        return new ResponseEntity<Map<String,String>>(response,HttpStatus.CREATED);
    }

    @PostMapping("/ticket")
    public ResponseEntity<Map<String,String>> buyTicket(@RequestBody @Valid BuyTicketRequest request) throws CheckTrainException {
        Map response = trainService.buyTicket(request);
        return new ResponseEntity<Map<String,String>>(response,HttpStatus.CREATED);
    }
}
