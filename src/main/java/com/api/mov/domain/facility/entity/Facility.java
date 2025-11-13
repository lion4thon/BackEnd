package com.api.mov.domain.facility.entity;


import com.api.mov.domain.pass.entity.Sport;
import com.api.mov.domain.reservation.entity.Reservation;
import com.api.mov.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Facility extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; //시설명

    @Column(nullable = false)
    private String contact; //연락처

    @Column(nullable = false)
    private String address; //시설 주소

    @Column(nullable = false)
    private String detailAddress; //상세 주소

    private String accessGuide; //오시는 길

    @Column(nullable = false)
    private int price; //업장 이용 가격

    @Column(nullable = false)
    private String postCode; //우편 번호

    private String Features; //업장 특징

    private String weekdayHours; //주간 영업시간
    private String weekendHours; //주말 영업시간
    private String holidayClosedInfo; //휴무 안내


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sport_id", nullable = false)
    private Sport sport;


    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

}
