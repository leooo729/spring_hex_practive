package com.example.spring_hex_practive.service;

import com.example.spring_hex_practive.controller.dto.request.BuyTicketRequest;
import com.example.spring_hex_practive.controller.dto.serviceAPI.GetTicketPriceResponse;
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
    private CheckTicket checkTicket;
    @Autowired
    private TrainRepo trainRepo;
    @Autowired
    private TrainTicketRepo trainTicketRepo;
    @Autowired
    private RestTemplate restTemplate;

    public Map<String, String> buyTicket(BuyTicketRequest request) throws CheckTrainException {

        multipleTrainTicketCheck(request);

        String ticketNO = java.util.UUID.randomUUID().toString().replace("-", "").toUpperCase();

        TrainTicket trainTicket = setTrainTicketInfo(request, ticketNO);

        trainTicketRepo.save(trainTicket);

        Map<String, String> response = new HashMap<>();
        response.put("uuid", ticketNO);
        return response;
    }

    //-----------------------------------------------------------------------------------method
    private void multipleTrainTicketCheck(BuyTicketRequest request) throws CheckTrainException {
        checkTicket.checkTrainNoNoExists(request.getTrain_no());
        checkTicket.checkStopSeq(request);
    }

    private Double getTicketPrice() {
        String url = "https://petstore.swagger.io/v2/store/inventory";
        ResponseEntity<GetTicketPriceResponse> responseEntity = restTemplate.getForEntity(url, GetTicketPriceResponse.class);
        return responseEntity.getBody().getString();
    }

    private TrainTicket setTrainTicketInfo(BuyTicketRequest request, String ticketNO) {

        TrainTicket trainTicket = new TrainTicket();

        trainTicket.setTicketNo(ticketNO);
        trainTicket.setTrainUuid(trainRepo.findUuidByTrainNo(request.getTrain_no()));
        trainTicket.setFromStop(request.getFrom_stop());
        trainTicket.setToStop(request.getTo_stop());
        trainTicket.setTakeDate(request.getTake_date());
        trainTicket.setPrice(getTicketPrice());

        return trainTicket;
    }
}
