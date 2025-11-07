package com.api.mov.domain.survey.entity;

import com.api.mov.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AvoidFactor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "avoid_factor_id")
    private Long id;

    @Column(name = "factor_name", nullable = false, length = 100)
    private String factorName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    @Setter
    private Survey survey;

}
