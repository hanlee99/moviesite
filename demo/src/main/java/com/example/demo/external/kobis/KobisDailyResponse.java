package com.example.demo.external.kobis;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KobisDailyResponse {
    private BoxOfficeResult boxOfficeResult;

    @Getter
    @Setter
    public static class BoxOfficeResult{
        private String boxofficeType;
        private String showRange;
        private List<DailyBoxOfficeItem> dailyBoxOfficeList;
    }

    @Getter
    @Setter
    public static class DailyBoxOfficeItem {
        private String rank;
        private String movieCd;
        private String movieNm;
        private String openDt;
        private String salesAcc;
        private String audiAcc;
    }

}
