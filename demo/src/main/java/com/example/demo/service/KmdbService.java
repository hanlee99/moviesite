package com.example.demo.service;

import com.example.demo.entity.Movie;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KmdbService {

    @Value("${api.kmdb.key}")
    private String KMDB_API_KEY;

    private final ObjectMapper mapper = new ObjectMapper();

    public Movie fetchMovieByTitleAndDate(String title, String openDt) {
        try {
            String url = String.format(
                    "https://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_json2.jsp?" +
                            "collection=kmdb_new2&ServiceKey=%s&detail=Y&title=%s&releaseDts=%s&releaseDte=%s",
                    KMDB_API_KEY,
                    URLEncoder.encode(title, StandardCharsets.UTF_8),
                    openDt, openDt
            );

            RestTemplate rest = new RestTemplate();
            String json = rest.getForObject(URI.create(url), String.class);
            JsonNode root = mapper.readTree(json);


            if (root == null || !root.has("Data") || root.path("Data").isEmpty()
                    || root.path("Data").get(0).path("Result").isEmpty()) {
                System.err.printf("KMDB: [%s, %s] 결과 없음 (Data/Result 비어 있음)%n", title, openDt);
                return null;
            }

            JsonNode result = root.path("Data").get(0).path("Result").get(0);
            if (result == null || result.isNull()) {
                System.err.printf("KMDB: [%s, %s] result 노드가 null%n", title, openDt);
                return null;
            }

            Movie movie = new Movie();
            movie.setTitle(result.path("title").asText(""));
            movie.setTitleEtc(result.path("titleEtc").asText(""));
            movie.setProdYear(result.path("prodYear").asText(""));
            movie.setDirectorNm(result.path("directors").path("director").get(0).path("directorNm").asText(""));
            movie.setActorNm(result.path("actors").path("actor").get(0).path("actorNm").asText(""));
            movie.setNation(result.path("nation").asText(""));
            movie.setCompany(result.path("company").asText(""));
            movie.setPlot(result.path("plots").path("plot").get(0).path("plotText").asText(""));
            movie.setRuntime(result.path("runtime").asText(""));
            movie.setRating(result.path("rating").asText(""));
            movie.setGenre(result.path("genre").asText(""));
            movie.setKmdbUrl(result.path("kmdbUrl").asText(""));
            movie.setType(result.path("type").asText(""));
            movie.setUseType(result.path("use").asText(""));
            movie.setRepRlsDate(result.path("repRlsDate").asText(""));
            movie.setPosters(result.path("posters").asText(""));
            movie.setStlls(result.path("stlls").asText(""));

            return movie;

        } catch (Exception e) {
            System.err.printf("요청 실패: %s (%s)%n", title, openDt);
            e.printStackTrace();
            return null;
        }
    }



}
