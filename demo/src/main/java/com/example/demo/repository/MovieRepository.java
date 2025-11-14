package com.example.demo.repository;

import com.example.demo.entity.MovieEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<MovieEntity, Long> {

    boolean existsByDocId(String docId);

    Optional<MovieEntity> findByMovieId(String movieId);


    // 제목 리스트로 조회
    List<MovieEntity> findByTitleIn(List<String> titles);

    @Query("SELECT m FROM MovieEntity m WHERE m.title LIKE %:title%")
    List<MovieEntity> findByTitleContains(@Param("title") String title);


    // 또는 제목 + 개봉일 (더 정확)
    Optional<MovieEntity> findByTitleAndRepRlsDate(String title, String repRlsDate);

    List<MovieEntity> findAllByTitleContainingIgnoreCase(String title);

    Page<MovieEntity> findByRepRlsDateLessThanEqualOrderByRepRlsDateDesc(String today, Pageable pageable);

    Page<MovieEntity> findByRepRlsDateGreaterThanOrderByRepRlsDateAsc(String today, Pageable pageable);
}
