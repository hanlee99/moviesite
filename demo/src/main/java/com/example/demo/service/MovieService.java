package com.example.demo.service;

import com.example.demo.entity.Movie;
import com.example.demo.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final KmdbService kmdbService;

    public Optional<Movie> findByTitleAndOpenDate(String title, String openDate) {
        return movieRepository.findByTitleAndRepRlsDateNormalized(title, openDate);
    }

    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    @Transactional
    public Movie findOrFetchAndSave(String title, String openDate) {
        String date = openDate.replace("-", "");    //박스오피스의 openDt가 - 를 포함
        return findByTitleAndOpenDate(title, openDate)
                .orElseGet(() -> {
                    // KMDB 요청
                    Movie fetched = kmdbService.fetchMovieByTitleAndDate(title, date);

                    if (fetched == null) {
                        System.err.printf("KMDB에서 [%s, %s] 데이터를 찾을 수 없습니다.%n", title, date);
                        return new Movie(); // 또는 null을 반환하지 않게 처리
                    }

                    // DB 저장
                    return save(fetched);
                });
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
    public void syncMovies2025() {
        String year = "2025";
        String startDate = year + "0101";
        String endDate = "20251231"; // ✅ 올해 말까지 (필요하면 LocalDate.now() + 60일 등으로)
        int pageSize = 100;
        int startCount = 0;

        System.out.printf("🚀 KMDB 갱신 시작 (%s ~ %s)%n", startDate, endDate);

        while (true) {
            // 1️⃣ 페이징 요청
            List<Movie> fetchedMovies = kmdbService.fetchMoviesBetweenPaged(startDate, endDate, pageSize, startCount);

            if (fetchedMovies.isEmpty()) {
                System.out.println("📭 더 이상 가져올 영화가 없습니다. 종료.");
                break;
            }

            System.out.printf("📦 [%d ~ %d] %d개 영화 수집%n", startCount + 1, startCount + fetchedMovies.size(), fetchedMovies.size());

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
                        System.out.printf("[갱신] %s (%s)%n", fetched.getTitle(), fetched.getRepRlsDate());
                    }
                } else {
                    movieRepository.save(fetched);
                    System.out.printf("[신규] %s (%s)%n", fetched.getTitle(), fetched.getRepRlsDate());
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

        System.out.println("✅ KMDB 영화 갱신 완료!");
    }


    /*public List<MovieWithBoxOfficeDto> getTest(String date){
        DailyBoxOffice box = new DailyBoxOffice();

        List<MovieWithBoxOfficeDto> res = new ArrayList<>();
        String[] posters = new String[10];
        res.add(new MovieWithBoxOfficeDto(box,
                new PosterResponse("12","http://file.koreafilm.or.kr/thm/02/99/19/14/tn_DPF031898.jpg")));
        res.add(new MovieWithBoxOfficeDto(box,
                new PosterResponse("22","http://file.koreafilm.or.kr/thm/02/99/19/12/tn_DPK024533.jpg")));
        res.add(new MovieWithBoxOfficeDto(box,
                new PosterResponse("33","http://file.koreafilm.or.kr/thm/02/99/19/13/tn_DPF031843.jpg")));
        res.add(new MovieWithBoxOfficeDto(box,
                new PosterResponse("44","http://file.koreafilm.or.kr/thm/02/99/19/16/tn_DPK024757.jpg")));
        res.add(new MovieWithBoxOfficeDto(box,
                new PosterResponse("55","http://file.koreafilm.or.kr/thm/02/99/19/11/tn_DPF031771.jpg")));
        res.add(new MovieWithBoxOfficeDto(box,
                new PosterResponse("66","http://file.koreafilm.or.kr/thm/02/99/19/17/tn_DPF032004.jpg")));
        res.add(new MovieWithBoxOfficeDto(box,
                new PosterResponse("77","http://file.koreafilm.or.kr/thm/02/99/18/82/tn_DPF030855.jpg")));
        res.add(new MovieWithBoxOfficeDto(box,
                new PosterResponse("88","http://file.koreafilm.or.kr/thm/02/99/19/11/tn_DPK024490.jpg")));
        res.add(new MovieWithBoxOfficeDto(box,
                new PosterResponse("99","http://file.koreafilm.or.kr/thm/02/99/19/13/tn_DPF031840.jpg")));
        res.add(new MovieWithBoxOfficeDto(box,
                new PosterResponse("100","http://file.koreafilm.or.kr/thm/02/99/19/15/tn_DPK024699.jpg")));

        return res;
    }*/
}
