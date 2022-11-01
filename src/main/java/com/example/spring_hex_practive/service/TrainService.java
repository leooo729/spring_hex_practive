package com.example.spring_hex_practive.service;

import com.example.spring_hex_practive.controller.dto.response.GetTargetTrainResponse;
import com.example.spring_hex_practive.controller.dto.response.Stops;
import com.example.spring_hex_practive.model.TrainRepo;
import com.example.spring_hex_practive.model.TrainStopRepo;
import com.example.spring_hex_practive.model.TrainTicketRepo;
import com.example.spring_hex_practive.model.entity.Train;
import com.example.spring_hex_practive.model.entity.TrainStop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrainService {
    @Autowired
    private TrainStopRepo trainStopRepo;
    @Autowired
    private TrainTicketRepo trainTicketRepo;
    @Autowired
    private TrainRepo trainRepo;

    public GetTargetTrainResponse getTargetTrain(int trainNo) {
        Train train = trainRepo.findByTrainNo(trainNo);
        List<TrainStop> trainStopList = trainStopRepo.findByTrainUuid(train.getUuid());
        List<Stops> stopsList = new ArrayList<>();
        for (TrainStop stop : trainStopList) {

        }


        return new GetTargetTrainResponse();
    }

}
