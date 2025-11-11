package com.example.demo.repository;

import com.example.demo.entity.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CinemaRepository extends JpaRepository<Cinema, Long> {
    Optional<Cinema> findByCinemaNameAndBrand_Name(String cinemaName, String brandName);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM cinema_specialtytheater", nativeQuery = true)
    void deleteAllCinemaSpecialties();
}
