package com.example.demo.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoxOfficeResultDto {
    private String type;
    private String showRange;
    private List<MovieWithBoxOfficeDto> movies;
}