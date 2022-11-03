package com.example.spring_hex_practive.service;

import org.springframework.stereotype.Component;

@Component
public class SwitchTrainKindSrevice {
    public String stringToCode(String trainName) {
        switch (trainName) {
            case "諾亞方舟號":
                return "A";
            case "霍格華茲號":
                return "B";
            default:
                break;
        }
        return "";
    }

    public String codeToString(String trainKind) {
        switch (trainKind) {
            case "A":
                return "諾亞方舟號";
            case "B":
                return "霍格華茲號";
            default:
                break;
        }
        return "";
    }
}
