package com.example.demo.service;

import com.example.demo.dto.movie.*;
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

    public BoxOfficeResultDto getBoxOfficeWithMovieResult(String targetDate, String type) {
        try {
            KobisOpenAPIRestService kobis = new KobisOpenAPIRestService(KOBIS_API_KEY);

            Map<String, String> params = new HashMap<>();
            params.put("targetDt", targetDate);
            //params.put("itemPerPage", "10");

            if (type.equalsIgnoreCase("weekly")) {
                params.put("weekGb", "0");
            }

            String json = switch (type.toLowerCase()) {
                case "weekly" -> kobis.getWeeklyBoxOffice(true, params);
                default -> kobis.getDailyBoxOffice(true, params);
            };

            BoxOfficeResponse response = mapper.readValue(json, BoxOfficeResponse.class);
            BoxOfficeResult result = response.getBoxOfficeResult();

            List<MovieWithBoxOfficeDto> movieDtos = new ArrayList<>();
            for (BoxOfficeItem box : result.getBoxOfficeList()) {
                Movie movie = movieService.findOrFetchAndSave(
                        box.getMovieNm(),
                        box.getOpenDt().replace("-", "")
                );
                movieDtos.add(MovieWithBoxOfficeDto.from(box, movie));
            }

            return new BoxOfficeResultDto(
                    result.getBoxofficeType(),
                    result.getShowRange(),
                    movieDtos
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new BoxOfficeResultDto("error", "", Collections.emptyList());
        }
    }
}
