package com.api.mov.domain.pass.entity;

import com.api.mov.global.coverter.SimpleStringListConverter;
import com.api.mov.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pass extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; //패키지 이름

    @Column(nullable = false)
    private int price; //패키지 가격

    @Column(nullable = false)
    private String description; //패키지 설명

    @Column(nullable = false)
    private Long viewCount = 0L;

    @OneToMany(mappedBy = "pass", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PassItem> passItems = new ArrayList<>();

    //유저 사이에 중간 매핑 테이블 필요
    @OneToMany(mappedBy = "pass", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserPass> userPassList = new ArrayList<>();
}
