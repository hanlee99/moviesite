package com.example.demo.service;

import com.example.demo.entity.Movie;
import com.example.demo.exception.MovieNotFoundException;
import com.example.demo.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final KmdbService kmdbService;

    public Movie getMovie(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));
        // 👆 영화 없으면 예외 발생!
    }

    public Optional<Movie> findByTitleAndOpenDate(String title, String openDate) {
        return movieRepository.findByTitleAndRepRlsDateNormalized(title, openDate);
    }

    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    @Transactional
    public Movie findOrFetchAndSave(String title, String openDate) {
        String date = openDate.replace("-", "");

        // 1. title + openDt 정확 검색
        Optional<Movie> exact = findByTitleAndOpenDate(title, openDate);

        if (exact.isPresent()) {
            return exact.get();
        }

        // 2. title-only 검색 (넓은 매칭)
        Optional<Movie> loose = movieRepository.findByTitle(title); // <= 이런 메서드만 만들면 됨

        if (loose.isPresent()) {
            return loose.get();
        }

        // 3. 그래도 없으면 KMDB 요청
        Movie fetched = kmdbService.fetchMovieByTitleAndDate(title, date);

        if (fetched == null) {
            log.error("KMDB에서 데이터를 찾을 수 없습니다: title={}, date={}", title, date);  // 👈 변경!
            return new Movie();
        }

        // 4. DB 저장
        return save(fetched);
    }


    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    //현재 상영중
    public List<Movie> getNowPlaying(int page, int size) {
        LocalDate today = LocalDate.now();
        String end = today.format(FMT);
        String start = today.minusDays(60).format(FMT);

        Pageable pageable = PageRequest.of(page, size);
        return movieRepository.findNowPlaying(start, end, pageable)
                .stream()
                .filter(m -> isValidDate(m.getRepRlsDate())) // ✅ 00으로 끝나는 날짜 제거
                .toList();
    }

    //개봉 예정
    public List<Movie> getUpcoming(int page, int size) {
        LocalDate today = LocalDate.now();
        String start = today.plusDays(1).format(FMT);
        String end = today.plusDays(60).format(FMT);

        Pageable pageable = PageRequest.of(page, size);

        return movieRepository.findUpcoming(start, end, pageable)
                .stream()
                .filter(m -> isValidDate(m.getRepRlsDate())) // ✅ 00으로 끝나는 날짜 제거
                .toList();
    }

    private boolean isValidDate(String date) {
        return date != null && date.length() == 8 && !date.endsWith("00");
    }

    @Transactional
    public void syncMovies(String year) {
        String startDate = year + "0101";
        String endDate = year + "1231";
        int pageSize = 100;
        int startCount = 0;

        while (true) {
            // 1️⃣ 페이징 요청
            List<Movie> fetchedMovies = kmdbService.fetchMoviesBetweenPaged(startDate, endDate, pageSize, startCount);

            if (fetchedMovies.isEmpty()) {
                log.info("더 이상 가져올 영화가 없습니다. 종료.");
                break;
            }

            // 2️⃣ DB 비교 및 갱신
            for (Movie fetched : fetchedMovies) {
                Optional<Movie> existingOpt = movieRepository.findByTitleAndRepRlsDateNormalized(
                        fetched.getTitle(),
                        fetched.getRepRlsDate()
                );

                if (existingOpt.isPresent()) {
                    Movie existing = existingOpt.get();
                    if (!Objects.equals(existing.getModDate(), fetched.getModDate())) {
                        existing.updateFrom(fetched);
                        movieRepository.save(existing);
                        log.info("[갱신] {} ({})", fetched.getTitle(), fetched.getRepRlsDate());  // 👈 변경!
                    }
                } else {
                    movieRepository.save(fetched);
                    log.info("[신규] {} ({})", fetched.getTitle(), fetched.getRepRlsDate());  // 👈 변경!
                }
            }

            // 3️⃣ 다음 페이지로 이동
            startCount += pageSize;

            try {
                Thread.sleep(500); // API 과부하 방지
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        log.info("KMDB 영화 갱신 완료!");
    }
}
