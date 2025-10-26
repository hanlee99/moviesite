package com.example.demo.service;

import com.example.demo.dto.movie.BoxOfficeResponse;
import com.example.demo.dto.movie.DailyBoxOffice;
import com.example.demo.dto.movie.MovieWithBoxOfficeDto;
import com.example.demo.entity.Movie;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.or.kobis.kobisopenapi.consumer.rest.KobisOpenAPIRestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BoxOfficeService {
    private final MovieService movieService;

    @Value("${api.kobis.key}")
    private String KOBIS_API_KEY;

    private final ObjectMapper mapper = new ObjectMapper();

    public List<MovieWithBoxOfficeDto> getBoxOfficeWithMovie(String targetDate, String type) {
        try {
            KobisOpenAPIRestService kobis = new KobisOpenAPIRestService(KOBIS_API_KEY);

            Map<String, String> params = new HashMap<>();
            params.put("targetDt", targetDate);
            params.put("itemPerPage", "10");

            String json = switch (type.toLowerCase()) {
                case "weekly" -> kobis.getWeeklyBoxOffice(true, params);
                default -> kobis.getDailyBoxOffice(true, params);
            };

            BoxOfficeResponse response = mapper.readValue(json, BoxOfficeResponse.class);
            List<DailyBoxOffice> list = response.getBoxOfficeResult().getDailyBoxOfficeList();
            List<MovieWithBoxOfficeDto> result = new ArrayList<>();

            for (DailyBoxOffice box : list) {
                Movie movie = movieService.findOrFetchAndSave(
                        box.getMovieNm(),
                        box.getOpenDt().replace("-", "")
                );

                result.add(MovieWithBoxOfficeDto.from(box, movie));

                System.out.printf("[%s] matched or fetched: %s%n",
                        box.getMovieNm(),
                        movie.getPosters() != null ? movie.getPosters() : "no poster");
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
