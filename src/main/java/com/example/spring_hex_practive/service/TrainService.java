package com.example.spring_hex_practive.service;

import com.example.spring_hex_practive.controller.dto.request.CreateTrainRequest;
import com.example.spring_hex_practive.controller.dto.response.GetTargetTrainResponse;
import com.example.spring_hex_practive.controller.dto.request.Stops;
import com.example.spring_hex_practive.controller.dto.response.TrainInfo;
import com.example.spring_hex_practive.exception.CheckTrainException;
import com.example.spring_hex_practive.exception.DataNotFoundException;
import com.example.spring_hex_practive.model.TrainRepo;
import com.example.spring_hex_practive.model.TrainStopRepo;
import com.example.spring_hex_practive.model.entity.Train;
import com.example.spring_hex_practive.model.entity.TrainStop;
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
    TrainStopRepo trainStopRepo;
    @Autowired
    TrainRepo trainRepo;
    @Autowired
    CheckTrain checkTrain;
    @Autowired
    SwitchTrainKind switchTrainKind;

    public GetTargetTrainResponse getTargetTrain(Integer trainNo) throws DataNotFoundException {

        Train train = trainRepo.findByTrainNo(trainNo);

        checkTrainNoExist(train);

        String trainName = switchTrainKind.switchKind(train.getTrainKind());

        List<Stops> stopsResponseList = setStopsResponseList(train);

        return new GetTargetTrainResponse(train.getTrainNo(), trainName, stopsResponseList);
    }

    public List<TrainInfo> getTrainByStop(String stopName) throws DataNotFoundException {

        List<String> trainUuidList = trainStopRepo.findTrainUuidByName(stopName);
        if(trainUuidList.isEmpty()){
            throw new DataNotFoundException("站名不存在");
        }

        List<TrainInfo> getTargetTrainList = setTargetTrainList(trainUuidList);

        return getTargetTrainList;
    }

    public Map<String, String> CreateTrainInfo(CreateTrainRequest request) throws CheckTrainException {
//---------------------------------------------------------------------set Train
        multipleTrainCheck(request);

        String trainUuid = java.util.UUID.randomUUID().toString().replace("-", "").toUpperCase();

        Train train = setTrainInfo(trainUuid, request);

        trainRepo.save(train);
//---------------------------------------------------------------------set TrainStop
        int seq = 1;
        for (Stops stop : request.getStops()) {

            TrainStop trainStop = setTrainStopInfo(trainUuid, stop);
            trainStop.setSeq(seq++);

            trainStopRepo.save(trainStop);
        }

        Map<String, String> response = new HashMap<>();
        response.put("uuid", trainUuid);
        return response;
    }

    //---------------------------------------------------------------------method
    private Train setTrainInfo(String trainUuid, CreateTrainRequest request) {
        String trainKindCode = switchTrainKind.switchKind(request.getTrain_kind());

        Train train = new Train();

        train.setUuid(trainUuid);
        train.setTrainNo(request.getTrain_no());
        train.setTrainKind(trainKindCode);
        return train;
    }

    private TrainStop setTrainStopInfo(String trainUuid, Stops stop) {
        String stopUuid = java.util.UUID.randomUUID().toString().replace("-", "").toUpperCase();
        TrainStop trainStop = new TrainStop();

        trainStop.setUuid(stopUuid);
        trainStop.setTrainUuid(trainUuid);
        trainStop.setName(stop.getStop_name());
        trainStop.setTime(LocalTime.parse(stop.getStop_time()));
        trainStop.setDeleteFlag("N");

        return trainStop;
    }

    private void multipleTrainCheck(CreateTrainRequest request) throws CheckTrainException {
        checkTrain.checkTrainNoAvailable(request.getTrain_no());
        checkTrain.multipleTrainCheck(request);
        checkTrain.checkTrainStopsSorted(request);
    }

    private void checkTrainNoExist(Train train) throws DataNotFoundException {
        if (train == null) {
            throw new DataNotFoundException("車次不存在");
        }
    }

    private List<Stops> setStopsResponseList(Train train) {
        List<Stops> stopsResponseList = new ArrayList<>();

        List<TrainStop> trainStopList = trainStopRepo.findByTrainUuid(train.getUuid());

        for (TrainStop stop : trainStopList) {
            Stops stops = new Stops();
            stops.setStop_name(stop.getName());
            stops.setStop_time(stop.getTime().toString());
            stopsResponseList.add(stops);
        }
        return stopsResponseList;
    }

    private List<TrainInfo> setTargetTrainList(List<String> trainUuidList) {

        List<TrainInfo> getTargetTrainList = new ArrayList<>();

        for (String trainUuid : trainUuidList) {
            Train train = trainRepo.findByUuid(trainUuid);
            String trainName = switchTrainKind.switchKind(train.getTrainKind());
            TrainInfo trainInfo = new TrainInfo(train.getTrainNo(), trainName);

            getTargetTrainList.add(trainInfo);
        }
        return getTargetTrainList;
    }
}
