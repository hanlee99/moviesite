package com.example.demo.dto.movie;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BoxOfficeItemDto {
    private int rank;
    private String title;
    private LocalDate openDate;
    private long accumulatedSales;
    private int audienceCount;
    private long accumulatedAudience;
}
