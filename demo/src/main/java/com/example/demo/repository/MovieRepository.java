package com.example.demo.repository;

import com.example.demo.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT m FROM Movie m " +
            "WHERE REPLACE(m.title, ' ', '') = REPLACE(:title, ' ', '') AND m.repRlsDate = :repRlsDate")
    Optional<Movie> findByTitleAndRepRlsDateNormalized(@Param("title") String title, @Param("repRlsDate") String repRlsDate);

    @Query("SELECT m FROM Movie m " +
            "WHERE REPLACE(m.title, ' ', '') LIKE CONCAT('%', REPLACE(:title, ' ', ''), '%')")
    Optional<Movie> findByTitleLoose(@Param("title") String title);

    @Query("""
    SELECT m FROM Movie m
    WHERE m.repRlsDate BETWEEN :start AND :end
    AND LENGTH(m.repRlsDate) = 8
    AND NOT m.repRlsDate LIKE '%00'
    ORDER BY m.repRlsDate DESC, m.title ASC
""")
    Page<Movie> findNowPlaying(
            @Param("start") String start,
            @Param("end") String end,
            Pageable pageable
    );

    @Query("""
    SELECT m FROM Movie m
    WHERE m.repRlsDate BETWEEN :start AND :end
    AND LENGTH(m.repRlsDate) = 8
    AND NOT m.repRlsDate LIKE '%00'
    ORDER BY m.repRlsDate ASC, m.title ASC
""")
    Page<Movie> findUpcoming(
            @Param("start") String start,
            @Param("end") String end,
            Pageable pageable
    );

}
