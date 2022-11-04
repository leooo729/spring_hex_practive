package com.example.spring_hex_practive.service;

import com.example.spring_hex_practive.controller.dto.request.CreateTrainRequest;
import com.example.spring_hex_practive.controller.dto.serviceAPI.CheckResponse;
import com.example.spring_hex_practive.controller.dto.request.Stops;
import com.example.spring_hex_practive.exception.CheckTrainException;
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

    public void checkTrainNoAvailable(Integer trainNo) throws CheckTrainException {

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
    public void multipleTrainCheck(CreateTrainRequest request) throws CheckTrainException {

        List<Map<String, String>> errorList = new ArrayList<>();

        checkTrainNoExists(request, errorList);
        checkTrainKindInvalid(request, errorList);
        checkTrainStopsDuplicate(request, errorList);

        if (!errorList.isEmpty()) {
            throw new CheckTrainException(errorList);
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
        //找出對應車種代碼
        String trainKindCode = switchTrainKind.switchKind(request.getTrain_kind());
        //資料庫無 報錯
        if (trainRepo.findByTrainKind(trainKindCode).isEmpty()) {
            Map<String, String> errorMessage = setErrorMessage("TrainKindInvalid", "Train Kind is invalid");
            errorList.add(errorMessage);
        }
        return errorList;
    }

    private List<Map<String, String>> checkTrainStopsDuplicate(CreateTrainRequest request, List<Map<String, String>> errorList) {
        //取得輸入站名list
        List<String> trainNameList = request.getStops().stream().map(stop -> stop.getStop_name()).collect(Collectors.toList());
        //對上面list做去重
        List<String> noDuplicateNameList = trainNameList.stream().distinct().collect(Collectors.toList());
        //原先list如與去重list不同 報錯
        if (trainNameList.size() != noDuplicateNameList.size()) {
            Map<String, String> errorMessage = setErrorMessage("TrainStopsDuplicate", "Train Stops is duplicate");
            errorList.add(errorMessage);
        }
        return errorList;
    }

    //--------------------------------------------------------------------------------------------------
    private final List<String> places = List.of("屏東", "高雄", "臺南", "嘉義", "彰化", "台中", "苗粟", "新竹", "桃園", "樹林",
            "板橋", "萬華", "台北", "松山", "南港", "汐止", "基隆");

    public void checkTrainStopsSorted(CreateTrainRequest request) throws CheckTrainException {
        //取得用時間排序的地名list
        List<String> sortedStopsList = request.getStops().stream().sorted(Comparator.comparing(Stops::getStop_time)).map(stop -> stop.getStop_name()).collect(Collectors.toList());

        List<Integer> stopNumbersList = new ArrayList<>();
        //找出地名對應的數字
        for (String s : sortedStopsList) {
            if (!places.contains(s))
                throwCheckTrainException("TrainStopPositionNotRight", "Train Stops [" + s + "] position is not exists");
            stopNumbersList.add(places.indexOf(s));
        }
        //當數字保持有序 前後相減 正負會一致 相乘始終>0 但順序出錯會有 +*- -*+出現 出現負值
        for (int now = 0; now < stopNumbersList.size() - 2; now++) {
            if (0 > (stopNumbersList.get(now) - stopNumbersList.get(now + 1)) * (stopNumbersList.get(now + 1) - stopNumbersList.get(now + 2)))
                throwCheckTrainException("TrainStopsNotSorted", "Train Stops is not sorted");
        }
    }

    //-------------------------------------------------------------------------------method
    private Map<String, String> setErrorMessage(String code, String message) {
        Map<String, String> errorMessage = new HashMap<>();
        errorMessage.put("code", code);
        errorMessage.put("message", message);
        return errorMessage;
    }

    private void throwCheckTrainException(String code, String message) throws CheckTrainException {
        List<Map<String, String>> errorList = new ArrayList<>();
        Map<String, String> errorMessage = setErrorMessage(code, message);
        errorList.add(errorMessage);
        throw new CheckTrainException(errorList);
    }
}

//根據Postman輸入時間上至下為排序 做判斷
//    public void checkTrainStopsSorted(List<Stops> stopsInfoList) throws CheckTrainException {
//
//        List<String> trainTimeList = stopsInfoList.stream().map(stop -> stop.getStop_time()).collect(Collectors.toList());
//        List<String> sortedTrainTimeList = stopsInfoList.stream().map(stop -> stop.getStop_time()).sorted().collect(Collectors.toList());
//
//        if (!trainTimeList.equals(sortedTrainTimeList)) {
//            throwCheckTrainException("TrainStopsNotSorted", "Train Stops is not sorted");
//        }
//    }
