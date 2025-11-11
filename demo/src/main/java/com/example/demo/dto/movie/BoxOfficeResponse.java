package com.example.demo.dto.movie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class BoxOfficeResponse {
    private BoxOfficeResult boxOfficeResult;
}
