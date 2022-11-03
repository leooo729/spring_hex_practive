package com.example.spring_hex_practive.service;

import com.example.spring_hex_practive.controller.dto.request.CreateTrainRequest;
import com.example.spring_hex_practive.controller.dto.response.CheckResponse;
import com.example.spring_hex_practive.controller.dto.response.Stops;
import com.example.spring_hex_practive.exception.CheckTrainException;
import com.example.spring_hex_practive.model.TrainRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CheckTrainService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TrainRepo trainRepo;
    @Autowired
    private SwitchTrainKindSrevice switchTrainKindSrevice;

    public void checkTrainNoAvailable(Integer trainNo) throws CheckTrainException {
        List<Map<String,String>> errorList = new ArrayList<>();
        String url = "https://petstore.swagger.io/v2/pet/" + trainNo;
        ResponseEntity<CheckResponse> responseEntity = restTemplate.getForEntity(url, CheckResponse.class);
        int code = responseEntity.getStatusCodeValue();
        if (code == 200) {
            CheckResponse checkResponse = responseEntity.getBody();
            if (!checkResponse.getStatus().equals("available")) {
                Map<String, String> map = new HashMap<>();
                map.put("code", "TrainNotAvailable");
                map.put("message", "Train is not available");
                errorList.add(map);
                throw new CheckTrainException(errorList);
            }
        }
    }

    public void multipleTrainCheck(CreateTrainRequest request) throws CheckTrainException {
        List<Map<String,String>> errorList = new ArrayList<>();
        //checkTrainNoExists
        if (trainRepo.findByTrainNo(request.getTrain_no()) != null) {
            Map<String, String> map = new HashMap<>();
            map.put("code", "TrainNoExists");
            map.put("message", "Train No is exists");
            errorList.add(map);
        }
        //checkTrainKindInvalid
        String trainKindCode=switchTrainKindSrevice.stringToCode(request.getTrain_kind());
        if (trainRepo.findByTrainKind(trainKindCode)==null) {
            Map<String, String> map = new HashMap<>();
            map.put("code", "TrainKindInvalid");
            map.put("message", "Train Kind is invalid");
            errorList.add(map);
        }
        //checkTrainStopsDuplicate
        List<String> trainNameList = request.getStops().stream().map(stop -> stop.getStop_name()).collect(Collectors.toList());
        List<String> noDuplicateNameList = trainNameList.stream().distinct().collect(Collectors.toList());
        if (trainNameList.size() != noDuplicateNameList.size()) {
            Map<String, String> map = new HashMap<>();
            map.put("code", "TrainStopsDuplicate");
            map.put("message", "Train Stops is duplicate");
            errorList.add(map);
        }
        if(!errorList.isEmpty()){
            throw new CheckTrainException(errorList);
        }

    }

    public void checkTrainStopsSorted(List<Stops> stopsInfoList) throws CheckTrainException {
        List<Map<String,String>> errorList = new ArrayList<>();
        List<String> trainTimeList = stopsInfoList.stream().map(stop -> stop.getStop_time()).collect(Collectors.toList());
        List<String> sortedTrainTimeList = stopsInfoList.stream().map(stop -> stop.getStop_time()).sorted().collect(Collectors.toList());
        if (!trainTimeList.equals(sortedTrainTimeList)) {
            Map<String, String> map = new HashMap<>();
            map.put("code", "TrainStopsNotSorted");
            map.put("message", "Train Stops is not sorted");
            errorList.add(map);
            throw new CheckTrainException(errorList);

        }
    }
}
