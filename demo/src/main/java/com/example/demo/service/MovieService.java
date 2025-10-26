package com.example.demo.service;

import com.example.demo.dto.movie.DailyBoxOffice;
import com.example.demo.dto.movie.MovieWithBoxOfficeDto;
import com.example.demo.dto.movie.PosterResponse;
import com.example.demo.entity.Movie;
import com.example.demo.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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
        String date = openDate.replace("-", "");
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
