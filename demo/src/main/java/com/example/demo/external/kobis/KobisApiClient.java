package com.example.demo.external.kobis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class KobisApiClient {
    private final WebClient webClient;

    private static final String BASE_URL = "https://kobis.or.kr/kobisopenapi/webservice/rest";

    @Value("${api.kobis.key}")
    private String KOBIS_API_KEY;

    public KobisDailyResponse getDailyBoxOffice(String date) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/boxoffice/searchDailyBoxOfficeList.json")
                        .queryParam("key", KOBIS_API_KEY)
                        .queryParam("targetDt", date)
                        .build())
                .retrieve()
                .bodyToMono(KobisDailyResponse.class)
                .block(); // → 동기로 변환 (너는 동기면 충분)
    }
}
