package com.example.demo.dto.movie;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DailyBoxOfficeDto {
    private String boxOfficeType;
    private String range;
    private List<BoxOfficeItemDto> items;
}
