package com.api.mov.domain.survey.entity;

import com.api.mov.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterestedSport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interested_sport_id")
    private Long id;

    @Column(name = "sport_name", nullable = false, length = 100)
    private String sportName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    @Setter
    private Survey survey;

}
