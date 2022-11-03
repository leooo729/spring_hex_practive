package com.example.spring_hex_practive.service;

import com.example.spring_hex_practive.controller.dto.response.GetTicketPriceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GetTicketPriceService {
    @Autowired
    private RestTemplate restTemplate;

    public Double getTicketPrice() {
        String url = "https://petstore.swagger.io/v2/store/inventory";
        ResponseEntity<GetTicketPriceResponse> responseEntity = restTemplate.getForEntity(url, GetTicketPriceResponse.class);
    return responseEntity.getBody().getString();
    }
}

