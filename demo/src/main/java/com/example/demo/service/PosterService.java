package com.example.demo.service;

import com.example.demo.dto.movie.PosterResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class PosterService {
    @Value("${api.kmdb.key}")
    private String API_KEY;
    private static final String BASE_URL =
            "http://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_json2.jsp";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public PosterResponse getPoster(String title, String openDt) {
        PosterResponse res = new PosterResponse();
        res.setTitle(title);

        try {
            StringBuilder urlBuilder = new StringBuilder(BASE_URL)
                    .append("?collection=kmdb_new2&detail=Y")
                    .append("&title=").append(URLEncoder.encode(title, StandardCharsets.UTF_8))
                    .append("&ServiceKey=").append(API_KEY);

            if (openDt != null && !openDt.isEmpty())
                urlBuilder.append("&releaseDts=").append(openDt);

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json; charset=UTF-8");

            // 🔹 한 번의 try로만 묶기
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            reader.close();

            JsonNode result = objectMapper
                    .readTree(sb.toString())
                    .path("Data").get(0)
                    .path("Result").get(0);

            String posters = result.path("posters").asText("");
            res.setPosterUrl(posters.isEmpty() ? "" : posters.split("\\|")[0].trim());

        } catch (Exception e) {
            res.setPosterUrl("");
        }

        return res;
    }
}
