package com.example.spring_hex_practive.service;

import com.example.spring_hex_practive.controller.dto.request.CreateTrainRequest;
import com.example.spring_hex_practive.controller.dto.serviceAPI.CheckResponse;
import com.example.spring_hex_practive.controller.dto.request.Stops;
import com.example.spring_hex_practive.exception.CheckErrorException;
import com.example.spring_hex_practive.model.TrainRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CheckTrain {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TrainRepo trainRepo;
    @Autowired
    private SwitchTrainKind switchTrainKind;

    public void checkTrainNoAvailable(Integer trainNo) throws CheckErrorException {

        String url = "https://petstore.swagger.io/v2/pet/" + trainNo;

        ResponseEntity<CheckResponse> responseEntity = restTemplate.getForEntity(url, CheckResponse.class);

        if (200 == responseEntity.getStatusCodeValue()) {
            CheckResponse checkResponse = responseEntity.getBody();
            if (!checkResponse.getStatus().equals("available")) {
                throwCheckTrainException("TrainNotAvailable", "Train is not available");
            }
        }
    }

    //-------------------------------------------------------------------------------------
    public void multipleTrainCheck(CreateTrainRequest request) throws CheckErrorException {

        List<Map<String, String>> errorList = new ArrayList<>();

        checkTrainNoExists(request, errorList);
        checkTrainKindInvalid(request, errorList);
        checkTrainStopsDuplicate(request, errorList);

        if (!errorList.isEmpty()) {
            throw new CheckErrorException(errorList);
        }
    }

    private List<Map<String, String>> checkTrainNoExists(CreateTrainRequest request, List<Map<String, String>> errorList) {
        if (trainRepo.findByTrainNo(request.getTrain_no()) != null) {
            Map<String, String> errorMessage = setErrorMessage("TrainNoExists", "Train No is exists");
            errorList.add(errorMessage);
        }
        return errorList;
    }

    private List<Map<String, String>> checkTrainKindInvalid(CreateTrainRequest request, List<Map<String, String>> errorList) {
        //????????????????????????
        String trainKindCode = switchTrainKind.switchKind(request.getTrain_kind());
        //???????????? ??????
        if (trainRepo.findByTrainKind(trainKindCode).isEmpty()) {
            Map<String, String> errorMessage = setErrorMessage("TrainKindInvalid", "Train Kind is invalid");
            errorList.add(errorMessage);
        }
        return errorList;
    }

    private List<Map<String, String>> checkTrainStopsDuplicate(CreateTrainRequest request, List<Map<String, String>> errorList) {
        //??????????????????list
        List<String> trainNameList = request.getStops().stream().map(stop -> stop.getStop_name()).collect(Collectors.toList());
        //?????????list?????????
        List<String> noDuplicateNameList = trainNameList.stream().distinct().collect(Collectors.toList());
        //??????list????????????list?????? ??????
        if (trainNameList.size() != noDuplicateNameList.size()) {
            Map<String, String> errorMessage = setErrorMessage("TrainStopsDuplicate", "Train Stops is duplicate");
            errorList.add(errorMessage);
        }
        return errorList;
    }

    //--------------------------------------------------------------------------------------------------
    //?????????????????????
    private final List<String> places = List.of("??????", "??????", "??????", "??????", "??????", "??????", "??????", "??????", "??????", "??????",
            "??????", "??????", "??????", "??????", "??????", "??????", "??????");

    public void checkTrainStopsSorted(CreateTrainRequest request,List<Stops> sortedStopsList) throws CheckErrorException {
        //??????????????????????????????list
        List<String>sortedStopsNameList=sortedStopsList.stream().map(stop -> stop.getStop_name()).collect(Collectors.toList());

        List<Integer> stopNumbersList = new ArrayList<>();
        //???????????????????????????
        for (String stopName : sortedStopsNameList) {
            if (!places.contains(stopName)) {
                throwCheckTrainException("TrainStopPositionNotRight", "Train Stops [" + stopName + "] position is not exists");
            }
            stopNumbersList.add(places.indexOf(stopName));
        }
        //????????????????????? ???????????? ??????????????? ????????????>0 ????????????????????? +*- -*+?????? ????????????
        for (int now = 0; now < stopNumbersList.size() - 2; now++) {
            if (0 > (stopNumbersList.get(now) - stopNumbersList.get(now + 1)) * (stopNumbersList.get(now + 1) - stopNumbersList.get(now + 2))) {
                throwCheckTrainException("TrainStopsNotSorted", "Train Stops is not sorted");
            }
        }
    }

    //-------------------------------------------------------------------------------method
    private Map<String, String> setErrorMessage(String code, String message) {
        Map<String, String> errorMessage = new HashMap<>();
        errorMessage.put("code", code);
        errorMessage.put("message", message);
        return errorMessage;
    }

    private void throwCheckTrainException(String code, String message) throws CheckErrorException {
        List<Map<String, String>> errorList = new ArrayList<>();
        Map<String, String> errorMessage = setErrorMessage(code, message);
        errorList.add(errorMessage);
        throw new CheckErrorException(errorList);
    }
}

//??????Postman?????????????????????????????? ?????????
//    public void checkTrainStopsSorted(List<Stops> stopsInfoList) throws CheckTrainException {
//
//        List<String> trainTimeList = stopsInfoList.stream().map(stop -> stop.getStop_time()).collect(Collectors.toList());
//        List<String> sortedTrainTimeList = stopsInfoList.stream().map(stop -> stop.getStop_time()).sorted().collect(Collectors.toList());
//
//        if (!trainTimeList.equals(sortedTrainTimeList)) {
//            throwCheckTrainException("TrainStopsNotSorted", "Train Stops is not sorted");
//        }
//    }
