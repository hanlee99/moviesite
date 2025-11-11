package com.example.demo.runner;

import com.example.demo.entity.Movie;
import com.example.demo.repository.MovieRepository;
import com.example.demo.service.KmdbService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PosterChangeService {

    private final KmdbService kmdbService;
    private final MovieRepository movieRepository;

    @Transactional
    public void updatePosterOrderFromKmdb(String title, String openDate, int newIndex) {
        // 1️⃣ KMDB에서 해당 영화 정보 가져오기
        Movie movieFromApi = kmdbService.fetchMovieByTitleAndDate(title, openDate);
        if (movieFromApi == null) {
            throw new IllegalStateException("KMDB에서 영화를 찾을 수 없습니다: " + title);
        }

        // 2️⃣ 기존 DB에서 영화 찾기
        Movie existing = movieRepository.findByTitleLoose(title)
                .orElseThrow(() -> new IllegalArgumentException("DB에 해당 영화가 없습니다: " + title));

        // 3️⃣ 포스터 문자열 분리
        String posters = movieFromApi.getPosters();
        if (posters == null || posters.isBlank()) {
            throw new IllegalStateException("KMDB 응답에 포스터 정보가 없습니다.");
        }

        String[] posterList = posters.split("\\|");
        if (newIndex < 1 || newIndex > posterList.length) {
            throw new IllegalArgumentException("인덱스가 범위를 벗어났습니다. (1~" + posterList.length + ")");
        }

        // 4️⃣ n번째 포스터를 앞으로 이동
        String selected = posterList[newIndex - 1];
        List<String> reordered = new ArrayList<>();
        reordered.add(selected);
        for (int i = 0; i < posterList.length; i++) {
            if (i != newIndex - 1) reordered.add(posterList[i]);
        }

        // 5️⃣ DB에 저장
        existing.setPosters(String.join("|", reordered));
        movieRepository.save(existing);

        System.out.printf("🎬 '%s'의 %d번째 포스터를 대표 포스터로 설정했습니다.%n", title, newIndex);
    }
}
