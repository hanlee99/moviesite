package com.example.demo.service;

import com.example.demo.dto.movie.BoxOfficeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.or.kobis.kobisopenapi.consumer.rest.KobisOpenAPIRestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BoxOfficeService {
    @Value("${api.kobis.key}")
    private String KOBIS_API_KEY;

    public BoxOfficeResponse getDailyBoxOffice(String targetDate){
        try{
            KobisOpenAPIRestService service = new KobisOpenAPIRestService(KOBIS_API_KEY);

            Map<String, String> params = new HashMap<>();
            params.put("targetDt", targetDate);

            String json = service.getDailyBoxOffice(true, params);

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, BoxOfficeResponse.class);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
