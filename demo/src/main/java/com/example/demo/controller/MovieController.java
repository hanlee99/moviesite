package com.example.demo.controller;


import com.example.demo.dto.movie.MovieWithPosterDto;
import com.example.demo.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/boxOfficeWithPoster")
    public List<MovieWithPosterDto> getBoxOfficeWithPoster(@RequestParam String date){
        return movieService.getBoxOfficeWithPosters(date);
    }


}