package com.example.spring_hex_practive.service;

import com.example.spring_hex_practive.controller.dto.request.BuyTicketRequest;
import com.example.spring_hex_practive.controller.dto.request.CreateTrainRequest;
import com.example.spring_hex_practive.controller.dto.response.*;
import com.example.spring_hex_practive.exception.DataNotFoundException;
import com.example.spring_hex_practive.exception.CheckTrainException;
import com.example.spring_hex_practive.model.TrainRepo;
import com.example.spring_hex_practive.model.TrainStopRepo;
import com.example.spring_hex_practive.model.TrainTicketRepo;
import com.example.spring_hex_practive.model.entity.Train;
import com.example.spring_hex_practive.model.entity.TrainStop;
import com.example.spring_hex_practive.model.entity.TrainTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TrainService {
    @Autowired
    private TrainStopRepo trainStopRepo;
    @Autowired
    private TrainTicketRepo trainTicketRepo;
    @Autowired
    private TrainRepo trainRepo;
    @Autowired
    private CheckTrainService checkTrainService;
    @Autowired
    private GetTicketPriceService getTicketPriceService;
    @Autowired
    private CheckTicketService checkTicketService;
    @Autowired
    private SwitchTrainKindSrevice switchTrainKindSrevice;

    public GetTargetTrainResponse getTargetTrain(int trainNo) throws DataNotFoundException {

        Train train = trainRepo.findByTrainNo(trainNo);
        if (train == null) {
            throw new DataNotFoundException("車次不存在");
        }

        List<TrainStop> trainStopList = trainStopRepo.findByTrainUuid(train.getUuid());
        List<Stops> stopsList = new ArrayList<>();
        String trainName = switchTrainKindSrevice.codeToString(train.getTrainKind());
        for (TrainStop stop : trainStopList) {
            Stops stops = new Stops(stop.getName(), stop.getTime().toString());
            stopsList.add(stops);
        }
        return new GetTargetTrainResponse(train.getTrainNo(), trainName, stopsList);
    }

    public List<TrainInfo> getTrainByStop(String stopName) {


        List<String> trainUuidList = trainStopRepo.findTrainUuidByName(stopName);
        List<TrainInfo> trainInfoList = new ArrayList<>();

        for (String trainUuid : trainUuidList) {
            Train train = trainRepo.findByUuid(trainUuid);
            String trainName = switchTrainKindSrevice.codeToString(train.getTrainKind());
            TrainInfo trainInfo = new TrainInfo(train.getTrainNo(), trainName);
            trainInfoList.add(trainInfo);
        }
        return trainInfoList;
    }

    public Map<String,String> CreateTrainInfo(CreateTrainRequest request) throws CheckTrainException {

        checkTrainService.checkTrainNoAvailable(request.getTrain_no());
        checkTrainService.multipleTrainCheck(request);
        checkTrainService.checkTrainStopsSorted(request.getStops());

        String trainCode = switchTrainKindSrevice.codeToString(request.getTrain_kind());

        String trainUuid = java.util.UUID.randomUUID().toString().replace("-", "").toUpperCase();

        Train train = new Train(trainUuid, request.getTrain_no(), trainCode);
        trainRepo.save(train);
//==================================================================TrainStop
        int seq = 1;
        for (Stops stop : request.getStops()) {

            String stopUuid = java.util.UUID.randomUUID().toString().replace("-", "").toUpperCase();
            TrainStop trainStop = new TrainStop();

            trainStop.setUuid(stopUuid);
            trainStop.setTrainUuid(trainUuid);
            trainStop.setSeq(seq++);
            trainStop.setName(stop.getStop_name());
            trainStop.setTime(LocalTime.parse(stop.getStop_time()));
            trainStop.setDeleteFlag("N");

            trainStopRepo.save(trainStop);
        }
        Map<String,String> map=new HashMap<>();
        map.put("uuid",trainUuid);
        return map;
    }

    public Map<String,String> buyTicket(BuyTicketRequest request) throws CheckTrainException {
        checkTicketService.checkTrainNoNoExists(request.getTrain_no());
        checkTicketService.checkStopSeq(request);

        TrainTicket trainTicket = new TrainTicket();
        String ticketNO = java.util.UUID.randomUUID().toString().replace("-", "").toUpperCase();

        trainTicket.setTicketNo(ticketNO);
        trainTicket.setTrainUuid(trainRepo.findUuidByTrainNo(request.getTrain_no()));
        trainTicket.setFromStop(request.getFrom_stop());
        trainTicket.setToStop(request.getTo_stop());
        trainTicket.setTakeDate(request.getTake_date());
        trainTicket.setPrice(getTicketPriceService.getTicketPrice());

        trainTicketRepo.save(trainTicket);
        Map<String,String> map=new HashMap<>();
        map.put("uuid",ticketNO);
        return map;
    }
}
