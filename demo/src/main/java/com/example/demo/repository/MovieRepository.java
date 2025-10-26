package com.example.demo.repository;

import com.example.demo.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT m FROM Movie m WHERE REPLACE(m.title, ' ', '') = REPLACE(:title, ' ', '') AND m.repRlsDate = :repRlsDate")
    Optional<Movie> findByTitleAndRepRlsDateNormalized(@Param("title") String title, @Param("repRlsDate") String repRlsDate);


}
