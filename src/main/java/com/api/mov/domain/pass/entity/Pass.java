package com.api.mov.domain.pass.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; //패키지 이름

    @Column(nullable = false)
    private int price; //패키지 가격

    @Column(nullable = false)
    private String description; //패키지 설명

    @OneToMany(mappedBy = "pass", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PassItem> passItems = new ArrayList<>();

    //유저 사이에 중간 매핑 테이블 필요
}
