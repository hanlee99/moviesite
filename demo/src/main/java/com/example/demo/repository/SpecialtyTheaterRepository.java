package com.example.demo.repository;

import com.example.demo.entity.SpecialtyTheater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpecialtyTheaterRepository extends JpaRepository<SpecialtyTheater, Long> {
    Optional<SpecialtyTheater> findByNameAndBrand_Name(String name, String brandName);
}
