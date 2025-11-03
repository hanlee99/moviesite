package com.example.demo.controller;

import com.example.demo.dto.movie.BoxOfficeResultDto;
import com.example.demo.dto.movie.MovieWithBoxOfficeDto;
import com.example.demo.entity.Movie;
import com.example.demo.service.BoxOfficeService;
import com.example.demo.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MovieService movieService;
    private final BoxOfficeService boxOfficeService;

    //홈: 기본은 일일 박스오피스
    @GetMapping("/")
    public String home(Model model) {

        String dailyDate = LocalDate.now().minusDays(1)
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String weeklyDate = LocalDate.now()
                .minusWeeks(1)
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        BoxOfficeResultDto daily = boxOfficeService.getBoxOfficeWithMovieResult(dailyDate, "daily");
        BoxOfficeResultDto weekly = boxOfficeService.getBoxOfficeWithMovieResult(weeklyDate, "weekly");

        List<Movie> nowPlaying = movieService.getNowPlaying(0,20);
        List<Movie> upcoming = movieService.getUpcoming(0,20);

        daily.setShowRange(dailyDate);

        model.addAttribute("daily", daily);
        model.addAttribute("weekly", weekly);
        model.addAttribute("nowPlaying", nowPlaying);
        model.addAttribute("upcoming", upcoming);
        model.addAttribute("type", "daily"); // 기본값

        return "home";
    }

    // 🎞주간 / 일간 토글 요청 (버튼으로 전환)
    @GetMapping("/boxoffice")
    public String showBoxOffice(@RequestParam(defaultValue = "daily") String type, Model model) {
        String dailyDate = LocalDate.now().minusDays(1)
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        BoxOfficeResultDto daily = boxOfficeService.getBoxOfficeWithMovieResult(dailyDate, "daily");

        model.addAttribute("dailyMovies", daily.getMovies());
        model.addAttribute("dailyRange", daily.getShowRange());
        model.addAttribute("type", type);
        return "home"; //동일한 템플릿 사용 (home.html)
    }

}
