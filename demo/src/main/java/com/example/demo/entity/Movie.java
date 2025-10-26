package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 500)
    private String titleEtc;

    private String prodYear;
    private String directorNm;

    @Column(columnDefinition = "TEXT")
    private String actorNm;

    private String nation;
    private String company;

    @Column(columnDefinition = "TEXT")
    private String plot;

    private String runtime;
    private String rating;

    @Column(length = 200)
    private String genre;

    @Column(length = 500)
    private String kmdbUrl;

    private String type;
    private String useType;
    private String repRlsDate;

    @Column(columnDefinition = "TEXT")
    private String releaseDates;

    private Boolean isReRelease;

    @Column(columnDefinition = "TEXT")
    private String posters;

    @Column(columnDefinition = "TEXT")
    private String stlls;

    @Column(columnDefinition = "TEXT")
    private String vodUrl;

    private String ratingNo;
    private String ratingGrade;
    private String modDate;
    private String regDate;
}
