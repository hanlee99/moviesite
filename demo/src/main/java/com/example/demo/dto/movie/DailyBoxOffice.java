package com.example.demo.dto.movie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DailyBoxOfficeDto {
    private String rank;
    private String movieNm;
    private String openDt;
    private String salesAcc;
    private String audiCnt;
    private String audiAcc;
}
