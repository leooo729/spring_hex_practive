package com.example.spring_hex_practive.service;

import com.example.spring_hex_practive.controller.dto.response.CheckResponse;
import com.example.spring_hex_practive.exception.TrainNoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Check {

    @Autowired
    private RestTemplate restTemplate;

    public void check(String trainNo) throws TrainNoException {
        String url = "https://petstore.swagger.io/v2/pet/" + trainNo;
        ResponseEntity<CheckResponse> responseEntity = restTemplate.getForEntity(url, CheckResponse.class);
        int code = responseEntity.getStatusCodeValue();
        if (code == 200) {
            CheckResponse checkResponse = responseEntity.getBody();
            if (!checkResponse.getStatus().equals("available")) {
                throw new TrainNoException();
            }
        }
    }
}
