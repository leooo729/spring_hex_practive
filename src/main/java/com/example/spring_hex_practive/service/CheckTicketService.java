package com.example.spring_hex_practive.service;

import com.example.spring_hex_practive.controller.dto.request.BuyTicketRequest;
import com.example.spring_hex_practive.exception.CheckTrainException;
import com.example.spring_hex_practive.model.TrainRepo;
import com.example.spring_hex_practive.model.TrainStopRepo;
import org.springframework.aop.AopInvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CheckTicketService {
    @Autowired
    private TrainRepo trainRepo;
    @Autowired
    private TrainStopRepo trainStopRepo;

    public void checkTrainNoNoExists(Integer trainNo) throws CheckTrainException {
        if (trainRepo.findByTrainNo(trainNo) == null) {
            List<Map<String,String>> errorList = new ArrayList<>();
            Map<String, String> map = new HashMap<>();
            map.put("code", "TrainNoNotExists");
            map.put("message", "Train No does not exists");
            errorList.add(map);
            throw new CheckTrainException(errorList);
        }
    }

    public void checkStopSeq(BuyTicketRequest request) throws CheckTrainException {
        try {
            String trainUuid = trainRepo.findUuidByTrainNo(request.getTrain_no());
            int fromStop = trainStopRepo.findTrainStopSeq(request.getFrom_stop(),trainUuid);
            int toStop = trainStopRepo.findTrainStopSeq(request.getTo_stop(), trainUuid);
            if (fromStop>=toStop){
                List<Map<String,String>> errorList = new ArrayList<>();
                Map<String, String> map = new HashMap<>();
                map.put("code", "TicketStopsInvalid");
                map.put("message", "Ticket From & To is invalid");
                errorList.add(map);
                throw new CheckTrainException(errorList);
            }
        }catch (AopInvocationException e){
            List<Map<String,String>> errorList = new ArrayList<>();
            Map<String, String> map = new HashMap<>();
            map.put("code", "StopsNameError");
            map.put("message", "Stop Name is Not Exist");
            errorList.add(map);
            throw new CheckTrainException(errorList);
        }

    }
}
