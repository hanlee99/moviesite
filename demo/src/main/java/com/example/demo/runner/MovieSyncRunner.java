package com.example.demo.runner;

import com.example.demo.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovieSyncRunner implements CommandLineRunner {
    private final MovieService movieService;

    @Override
    public void run(String... args) {
        System.out.println("🎬 KMDB 영화 동기화 시작...");
        movieService.syncMovies("2025");
        System.out.println("✅ 동기화 완료!");
    }
}
