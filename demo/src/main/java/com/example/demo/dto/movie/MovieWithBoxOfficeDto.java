package com.example.demo.dto.movie;

import com.example.demo.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieWithBoxOfficeDto {

    private String rank;
    private String title;
    private String openDt;
    private String posterUrl;
    private String genre;
    private String plot;
    private String salesAcc;
    private String audiCnt;
    private String audiAcc;

    // DailyBoxOffice + MovieEntity 결합
    public static  MovieWithBoxOfficeDto from(DailyBoxOffice box, Movie movie) {
        MovieWithBoxOfficeDto dto = new MovieWithBoxOfficeDto();
        dto.setRank(box.getRank());
        dto.setTitle(box.getMovieNm());
        dto.setOpenDt(box.getOpenDt());
        dto.setSalesAcc(box.getSalesAcc());
        dto.setAudiCnt(box.getAudiCnt());
        dto.setAudiAcc(box.getAudiAcc());

        if (movie != null) {
            String poster = movie.getPosters();
            if (poster != null && !poster.isBlank()) {
                // |, , 둘 다 고려해서 첫 번째 유효한 URL 찾기
                String[] candidates = poster.split("[|,]");
                String firstPoster = Arrays.stream(candidates)
                        .map(String::trim)
                        .filter(s -> s.startsWith("http"))
                        .findFirst()
                        .orElse("");

                dto.setPosterUrl(firstPoster);
            } else {
                dto.setPosterUrl(""); // 포스터 없음
            }

            dto.setGenre(movie.getGenre());
            dto.setPlot(movie.getPlot());
        }
        System.out.println(box.getMovieNm() + " | " + box.getOpenDt());
        System.out.println("movie found" + (movie != null ? movie.getTitle() : "실패"));

        return dto;
    }

    // 박스오피스 정보만 있을 때 (포스터X)
    public static MovieWithBoxOfficeDto fromBoxOfficeOnly(DailyBoxOffice box) {
        MovieWithBoxOfficeDto dto = new MovieWithBoxOfficeDto();
        dto.setRank(box.getRank());
        dto.setTitle(box.getMovieNm());
        dto.setOpenDt(box.getOpenDt());
        dto.setSalesAcc(box.getSalesAcc());
        dto.setAudiCnt(box.getAudiCnt());
        dto.setAudiAcc(box.getAudiAcc());
        return dto;
    }
}
