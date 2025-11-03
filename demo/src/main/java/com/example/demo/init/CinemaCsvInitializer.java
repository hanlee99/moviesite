package com.example.demo.init;

import com.example.demo.entity.Brand;
import com.example.demo.entity.Cinema;
import com.example.demo.entity.Region;
import com.example.demo.repository.BrandRepository;
import com.example.demo.repository.CinemaRepository;
import com.example.demo.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.opencsv.CSVReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CinemaCsvInitializer implements CommandLineRunner {

    private final BrandRepository brandRepository;
    private final RegionRepository regionRepository;
    private final CinemaRepository cinemaRepository;

    @Override
    public void run(String... args) throws Exception {
        if (cinemaRepository.count() > 0) {
            System.out.println("🎦 기존 Cinema 데이터가 존재하므로 CSV 로드를 건너뜁니다.");
            return;
        }

        System.out.println("CSV 데이터 로드 시작...");

        var inputStream = getClass().getResourceAsStream("/data/cinema-data.csv");
        if (inputStream == null) {
            throw new IllegalStateException("CSV 파일을 찾을 수 없습니다! (경로 확인: /data/cinema-data.csv)");
        }
        System.out.println("CSV 파일 로드 성공!");



        // resources 폴더의 CSV 파일 읽기
        try (CSVReader reader = new CSVReader(
                new InputStreamReader(
                        getClass().getResourceAsStream("/data/cinema-data.csv"),
                        StandardCharsets.UTF_8
                ))) {

            reader.readNext(); // 첫 줄(헤더) 건너뛰기
            String[] parts;
            while ((parts = reader.readNext()) != null) {
                if (parts.length < 10) continue;

                String cinemaName = parts[0].trim();
                String brandName = parts[1].trim();
                String businessStatus = parts[2].trim();
                String classificationRegion = parts[3].trim();
                String regionalLocal = parts[4].trim();
                String basicLocal = parts[5].trim();
                String streetAddress = parts[6].trim();
                String loadAddress = parts[7].trim();
                String xStr = parts[8].replaceAll("[^0-9.]", "").trim(); // x
                String yStr = parts[9].replaceAll("[^0-9.]", "").trim(); // y

                if (xStr.isEmpty() || yStr.isEmpty()) {
                    System.out.println("좌표 누락: " + cinemaName + " → 스킵");
                    continue;
                }

                BigDecimal longitude = new BigDecimal(xStr);
                BigDecimal latitude = new BigDecimal(yStr);

                // 브랜드 중복 체크 후 저장
                Brand brand = brandRepository.findAll()
                        .stream()
                        .filter(b -> b.getName().equals(brandName))
                        .findFirst()
                        .orElseGet(() -> brandRepository.save(new Brand(null, brandName)));

                // 지역 중복 체크 후 저장
                Region region = regionRepository.findAll()
                        .stream()
                        .filter(r -> r.getRegionalLocal().equals(regionalLocal) && r.getBasicLocal().equals(basicLocal))
                        .findFirst()
                        .orElseGet(() -> regionRepository.save(new Region(null, regionalLocal, basicLocal)));

                // 영화관 저장
                Cinema cinema = new Cinema();
                cinema.setCinemaName(cinemaName);
                cinema.setBusinessStatus(businessStatus);
                cinema.setClassificationRegion(classificationRegion);
                cinema.setStreetAddress(streetAddress);
                cinema.setLoadAddress(loadAddress);
                cinema.setLatitude(latitude);
                cinema.setLongitude(longitude);
                cinema.setBrand(brand);
                cinema.setRegion(region);
                cinemaRepository.save(cinema);
            }
        }
        System.out.println("CSV 데이터 500행 로드 완료!");
    }
}

