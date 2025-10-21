package com.example.demo.service;

import com.example.demo.dto.movie.BoxOfficeResponse;
import com.example.demo.dto.movie.DailyBoxOfficeDto;
import com.example.demo.dto.movie.MovieWithPosterDto;
import com.example.demo.dto.movie.PosterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final BoxOfficeService boxofficeService;    //KOBIS 영화 정보 가져오기
    private final PosterService posterService;    //KMDB 영화 포스터 가져오기

    public List<MovieWithPosterDto> getBoxOfficeWithPosters(String date){
        BoxOfficeResponse response = boxofficeService.getDailyBoxOffice(date);
        if(response == null || response.getBoxOfficeResultDto() == null)
            return new ArrayList<>();

        List<DailyBoxOfficeDto> boxOfficeList = response.getBoxOfficeResultDto().getDailyBoxOfficeDtoList();
        List<MovieWithPosterDto> result = new ArrayList<>();

        for(DailyBoxOfficeDto movie : boxOfficeList){
            PosterResponse posterResponse = posterService.getPoster(movie.getMovieNm(), movie.getOpenDt());
            result.add(new MovieWithPosterDto(movie, posterResponse));
        }

        return result;
    }

    public List<MovieWithPosterDto> getTest(String date){
        DailyBoxOfficeDto box = new DailyBoxOfficeDto();

        List<MovieWithPosterDto> res = new ArrayList<>();
        String[] posters = new String[10];
        res.add(new MovieWithPosterDto(box,
                new PosterResponse("12","http://file.koreafilm.or.kr/thm/02/99/19/14/tn_DPF031898.jpg")));
        res.add(new MovieWithPosterDto(box,
                new PosterResponse("22","http://file.koreafilm.or.kr/thm/02/99/19/12/tn_DPK024533.jpg")));
        res.add(new MovieWithPosterDto(box,
                new PosterResponse("33","http://file.koreafilm.or.kr/thm/02/99/19/13/tn_DPF031843.jpg")));
        res.add(new MovieWithPosterDto(box,
                new PosterResponse("44","http://file.koreafilm.or.kr/thm/02/99/19/16/tn_DPK024757.jpg")));
        res.add(new MovieWithPosterDto(box,
                new PosterResponse("55","http://file.koreafilm.or.kr/thm/02/99/19/11/tn_DPF031771.jpg")));
        res.add(new MovieWithPosterDto(box,
                new PosterResponse("66","http://file.koreafilm.or.kr/thm/02/99/19/17/tn_DPF032004.jpg")));
        res.add(new MovieWithPosterDto(box,
                new PosterResponse("77","http://file.koreafilm.or.kr/thm/02/99/18/82/tn_DPF030855.jpg")));
        res.add(new MovieWithPosterDto(box,
                new PosterResponse("88","http://file.koreafilm.or.kr/thm/02/99/19/11/tn_DPK024490.jpg")));
        res.add(new MovieWithPosterDto(box,
                new PosterResponse("99","http://file.koreafilm.or.kr/thm/02/99/19/13/tn_DPF031840.jpg")));
        res.add(new MovieWithPosterDto(box,
                new PosterResponse("100","http://file.koreafilm.or.kr/thm/02/99/19/15/tn_DPK024699.jpg")));
        res.add(new MovieWithPosterDto(box,
                new PosterResponse("100","http://file.koreafilm.or.kr/thm/02/99/19/15/tn_DPK024699.jpg")));
        res.add(new MovieWithPosterDto(box,
                new PosterResponse("100","http://file.koreafilm.or.kr/thm/02/99/19/15/tn_DPK024699.jpg")));
        return res;
    }


}
