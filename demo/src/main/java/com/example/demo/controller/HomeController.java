package com.example.demo.controller;

import com.example.demo.dto.movie.MovieWithPosterDto;
import com.example.demo.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MovieService movieService;
    String  date;

    @GetMapping("/")
    public String home(Model model){
        boolean test = true;

        if(test){

            List<MovieWithPosterDto> res = movieService.getTest(date);
            model.addAttribute("movies", res);
            return "home";
        }


        LocalDate target = LocalDate.now().minusDays(1);
        String date = target.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        List<MovieWithPosterDto> response = movieService.getBoxOfficeWithPosters(date);

        model.addAttribute("movies", response);

        return "home";
    }
}
