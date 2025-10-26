package com.example.demo.controller;

import com.example.demo.dto.movie.MovieWithBoxOfficeDto;
import com.example.demo.service.BoxOfficeService;
import com.example.demo.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoxOfficeController {
    private final MovieService movieService;
    String  date;
    private final BoxOfficeService boxOfficeService;

    //홈: 기본은 일일 박스오피스
    @GetMapping("/")
    public String home(Model model) {

        String date = LocalDate.now().minusDays(1)
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        List<MovieWithBoxOfficeDto> movies =
                boxOfficeService.getBoxOfficeWithMovie(date, "daily");

        model.addAttribute("movies", movies);
        model.addAttribute("type", "daily"); // 기본값
        return "home";
    }

    // 🎞주간 / 일간 토글 요청 (버튼으로 전환)
    @GetMapping("/boxoffice")
    public String showBoxOffice(@RequestParam(defaultValue = "daily") String type, Model model) {
        String date = LocalDate.now().minusDays(1)
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        List<MovieWithBoxOfficeDto> movies =
                boxOfficeService.getBoxOfficeWithMovie(date, type);

        for (MovieWithBoxOfficeDto movie : movies) {
            System.out.println(movie.getTitle() + " -> " + movie.getPosterUrl());
        }

        model.addAttribute("movies", movies);
        model.addAttribute("type", type);
        return "home"; //동일한 템플릿 사용 (home.html)
    }
}
