package com.example.demo.dto.movie;

import lombok.Data;

import java.util.List;

@Data
public class BoxOfficeResultDto {
    private String boxofficeType;
    private String showRange;
    private List<DailyBoxOfficeDto> dailyBoxOfficeDtoList;
}
