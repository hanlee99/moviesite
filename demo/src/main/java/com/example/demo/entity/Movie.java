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

    public void updateFrom(Movie src) {
        this.titleEtc = src.getTitleEtc();
        this.prodYear = src.getProdYear();
        this.directorNm = src.getDirectorNm();
        this.actorNm = src.getActorNm();
        this.nation = src.getNation();
        this.company = src.getCompany();
        this.plot = src.getPlot();
        this.runtime = src.getRuntime();
        this.rating = src.getRating();
        this.genre = src.getGenre();
        this.kmdbUrl = src.getKmdbUrl();
        this.type = src.getType();
        this.useType = src.getUseType();
        this.repRlsDate = src.getRepRlsDate();
        this.releaseDates = src.getReleaseDates();
        this.isReRelease = src.getIsReRelease();
        this.posters = src.getPosters();
        this.stlls = src.getStlls();
        this.vodUrl = src.getVodUrl();
        this.ratingNo = src.getRatingNo();
        this.ratingGrade = src.getRatingGrade();
        this.modDate = src.getModDate();
        this.regDate = src.getRegDate();
    }
}
