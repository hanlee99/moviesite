package com.example.demo.controller;

import com.example.demo.entity.Movie;
import com.example.demo.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/{id}")
    public Movie getMovie(@PathVariable Long id) {
        return movieService.getMovie(id);
    }

    @GetMapping("/now-playing")
    public List<Movie> getNowPlaying(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return movieService.getNowPlaying(page,size);
    }

    @GetMapping("/upcoming")
    public List<Movie> getUpcoming(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return movieService.getUpcoming(page,size);
    }
}
