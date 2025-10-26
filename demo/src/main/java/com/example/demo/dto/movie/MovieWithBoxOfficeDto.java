package com.example.demo.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieWithPosterDto {
    private DailyBoxOffice info;
    private PosterResponse poster;
}
