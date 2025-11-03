package com.example.demo.dto.movie;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class BoxOfficeResult {
    private String boxofficeType;
    private String showRange;
    private String yearWeekTime;

    @JsonProperty("dailyBoxOfficeList")
    private List<BoxOfficeItem> dailyList;

    @JsonProperty("weeklyBoxOfficeList")
    private List<BoxOfficeItem> weeklyList;

    public List<BoxOfficeItem> getBoxOfficeList() {
        if (dailyList != null) return dailyList;
        if (weeklyList != null) return weeklyList;
        return Collections.emptyList();
    }
}
