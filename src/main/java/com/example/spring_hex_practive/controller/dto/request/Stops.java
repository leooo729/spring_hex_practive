package com.example.spring_hex_practive.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stops {
    private String stop_name;
    @Pattern(regexp = "(^([0-1]?\\d|2[0-3]):([0-5]?\\d)$)",message = "時間格式錯誤")
    private String stop_time;

}
