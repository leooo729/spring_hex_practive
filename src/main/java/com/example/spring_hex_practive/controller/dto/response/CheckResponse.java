package com.example.spring_hex_practive.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckResponse {
    private String id;
    private IdName category;
    private String name;
    private List photoUrls;
    private List<IdName> tags;
    private String status;
}
class IdName{
    private String id;
    private String name;
}