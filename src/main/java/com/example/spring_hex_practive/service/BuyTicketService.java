package com.example.spring_hex_practive.service;

import com.example.spring_hex_practive.controller.dto.request.BuyTicketRequest;
import com.example.spring_hex_practive.controller.dto.response.GetTicketPriceResponse;
import com.example.spring_hex_practive.exception.CheckTrainException;
import com.example.spring_hex_practive.model.TrainRepo;
import com.example.spring_hex_practive.model.TrainTicketRepo;
import com.example.spring_hex_practive.model.entity.TrainTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class BuyTicketService {
    @Autowired
    private CheckTicketService checkTicketService;
    @Autowired
    private TrainRepo trainRepo;
    @Autowired
    private TrainTicketRepo trainTicketRepo;
    @Autowired
    private RestTemplate restTemplate;

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
        trainTicket.setPrice(getTicketPrice());

        trainTicketRepo.save(trainTicket);
        Map<String,String> map=new HashMap<>();
        map.put("uuid",ticketNO);
        return map;
    }
    //================================================================================
    private Double getTicketPrice() {
        String url = "https://petstore.swagger.io/v2/store/inventory";
        ResponseEntity<GetTicketPriceResponse> responseEntity = restTemplate.getForEntity(url, GetTicketPriceResponse.class);
        return responseEntity.getBody().getString();
    }
}
