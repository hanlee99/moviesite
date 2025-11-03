package com.example.demo.dto.movie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class BoxOfficeItem {
    private String rank;
    private String movieNm; //title과 같은 영화명(국문)
    private String openDt;
    private String salesAcc;
    private String audiCnt;
    private String audiAcc;
}
