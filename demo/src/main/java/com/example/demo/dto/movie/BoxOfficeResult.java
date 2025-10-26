package com.example.demo.dto.movie;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoxOfficeResult {
    private String boxofficeType;
    private String showRange;
    private List<DailyBoxOffice> dailyBoxOfficeList;
}
