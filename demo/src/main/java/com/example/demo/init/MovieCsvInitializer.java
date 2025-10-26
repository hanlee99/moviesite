package com.example.demo.init;

import com.example.demo.entity.Movie;
import com.example.demo.repository.MovieRepository;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class MovieCsvInitializer implements CommandLineRunner {

    private final MovieRepository movieRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("KMDB CSV 로드 시작...");

        var inputStream = getClass().getResourceAsStream("/data/KMDB_2025.csv");
        if (inputStream == null) {
            throw new IllegalStateException("CSV 파일을 찾을 수 없습니다! (/resources/data 경로 확인)");
        }

        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String[] headers = reader.readNext(); // 헤더 스킵
            String[] line;
            int count = 0;


            while ((line = reader.readNext()) != null) {
                // 각 컬럼 순서에 맞게 인덱스 지정 (Python csv의 열 순서 그대로)
                Movie movie = Movie.builder()
                        .title(line[0])
                        .titleEtc(line[1])
                        .prodYear(line[2])
                        .directorNm(line[3])
                        .actorNm(line[4])
                        .nation(line[5])
                        .company(line[6])
                        .plot(line[7])
                        .runtime(line[8])
                        .rating(line[9])
                        .genre(line[10])
                        .kmdbUrl(line[11])
                        .type(line[12])
                        .useType(line[13])
                        .repRlsDate(line[14].replace("|",""))
                        .releaseDates(line[15])
                        .isReRelease(Boolean.valueOf(line[16]))
                        .posters(line[17])
                        .stlls(line[18])
                        .vodUrl(line[19])
                        .ratingNo(line[20])
                        .ratingGrade(line[21])
                        .modDate(line[22])
                        .regDate(line[23])
                        .build();
                System.out.println("열 개수: " + line.length);
                System.out.println(Arrays.toString(line));
                movieRepository.save(movie);

                System.out.println("🎬 " + movie.getTitle() + " → " + movie.getPosters());

                count++;
            }

            System.out.println("총 " + count + "편 저장 완료! (H2 DB에 반영됨)");
        }
    }
}
