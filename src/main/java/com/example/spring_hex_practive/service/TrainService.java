package com.example.spring_hex_practive.service;

import com.example.spring_hex_practive.controller.dto.request.CreateTrainRequest;
import com.example.spring_hex_practive.controller.dto.response.CreateTrainResponse;
import com.example.spring_hex_practive.controller.dto.response.GetTargetTrainResponse;
import com.example.spring_hex_practive.controller.dto.response.Stops;
import com.example.spring_hex_practive.controller.dto.response.GetTrainByStopResponse;
import com.example.spring_hex_practive.controller.dto.response.TrainInfo;
import com.example.spring_hex_practive.exception.DataNotFoundException;
import com.example.spring_hex_practive.model.TrainRepo;
import com.example.spring_hex_practive.model.TrainStopRepo;
import com.example.spring_hex_practive.model.TrainTicketRepo;
import com.example.spring_hex_practive.model.entity.Train;
import com.example.spring_hex_practive.model.entity.TrainStop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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


    public GetTargetTrainResponse getTargetTrain(int trainNo) throws DataNotFoundException {

        Train train = trainRepo.findByTrainNo(trainNo);
        if (train == null) {
            throw new DataNotFoundException("車次不存在");
        }

        List<TrainStop> trainStopList = trainStopRepo.findByTrainUuid(train.getUuid());
        List<Stops> stopsList = new ArrayList<>();
        for (TrainStop stop : trainStopList) {
            Stops stops = new Stops(stop.getName(), stop.getTime().toString());
            stopsList.add(stops);
        }

        return new GetTargetTrainResponse(train.getTrainNo(), train.getTrainKind(), stopsList);
    }

    public List<TrainInfo> getTrainByStop(String stopName) {

        Map<String, String> map = Map.of("A", "諾亞方舟號", "B", "霍格華茲號");

        List<String> trainUuidList = trainStopRepo.findTrainUuidByName(stopName);
        List<TrainInfo> trainInfoList = new ArrayList<>();

        for (String trainUuid : trainUuidList) {
            Train train = trainRepo.findByUuid(trainUuid);
            TrainInfo trainInfo = new TrainInfo(train.getTrainNo(), map.get(train.getTrainKind()));
            trainInfoList.add(trainInfo);
        }
        return trainInfoList;
    }

    public CreateTrainResponse CreateTrainInfo(CreateTrainRequest request) {

        Map<String, String> map = Map.of("諾亞方舟號", "A", "霍格華茲號", "B");

        String trainUuid = java.util.UUID.randomUUID().toString().replace("-", "").toUpperCase();

        Train train = new Train(trainUuid, request.getTrain_no(), map.get(request.getTrain_kind()));
        trainRepo.save(train);
//==================================================================TrainStop
        int seq=1;
        for(Stops stop:request.getStops()){
            String stopUuid = java.util.UUID.randomUUID().toString().replace("-", "").toUpperCase();
            TrainStop trainStop=new TrainStop(stopUuid
                    ,trainUuid
                    ,seq++
                    ,stop.getStop_name()
                    , LocalTime.parse(stop.getStop_time())
                    ,"N");
            trainStopRepo.save(trainStop);
        }
        return new CreateTrainResponse(trainUuid);
    }


}
