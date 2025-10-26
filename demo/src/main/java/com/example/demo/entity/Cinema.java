package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CinemaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cinemaId;

    @Column(length = 100)
    private String cinemaName;

    @Column(length = 20)
    private String businessStatus;

    @Column(length = 20)
    private String classificationRegion;

    @Column(length = 255)
    private String streetAddress;

    @Column(length = 255)
    private String loadAddress;

    @Column(precision = 15, scale = 8)
    private BigDecimal longitude;

    @Column(precision = 15, scale = 8)
    private BigDecimal latitude;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private BrandEntity brandEntity;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private RegionEntity regionEntity;

}
