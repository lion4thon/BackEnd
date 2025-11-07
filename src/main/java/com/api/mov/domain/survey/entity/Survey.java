package com.api.mov.domain.survey.entity;

import com.api.mov.domain.survey.web.dto.CreateSurveyReq;
import com.api.mov.domain.user.entity.User;
import com.api.mov.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import com.api.mov.domain.pass.entity.Sport;
import com.api.mov.domain.survey.entity.InterestedSport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "surveys")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Survey extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_id")
    private Long id;

    @Column(nullable = false, length = 500)
    private String purpose; // 운동의 목적

    @Column(name = "preferred_time", nullable = false, length = 100)
    private String preferredTime; // 운동 선호 시간

    @Column(nullable = false)
    private Long price; // 1회 기준 패키지의 적정 가격

    @Column(name = "preferred_intensity", nullable = false)
    private String preferredIntensity; // 운동의 강도

    @Column(name = "recovery_condition", nullable = false)
    private String recoveryCondition; // 운동 후 회복 정도

    @Column(name = "preferred_environment", nullable = false)
    private String preferredEnvironment; // 선호하는 운동 환경

    @Column(name = "time_range", nullable = false)
    private String timeRange; // 이동 가능 시간

    @Column()
    private String answer; // 피하고 싶은 요소가 있다면 알려주세요


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 다중 응답 - 별도 테이블
    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AvoidFactor> avoidFactors = new ArrayList<>();

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InterestedSport> interestedSports = new ArrayList<>();

    //== DTO를 Entity로 변환하는 정적 팩토리 메소드 (생성 로직) ==//
    public static Survey createFromDto(CreateSurveyReq dto,User user, List<Sport> sports) {
        Survey survey = Survey.builder()
                .user(User.builder().build())
                .purpose(dto.getPurpose())
                .preferredTime(dto.getPreferredTime())
                .price(dto.getPrice())
                .preferredIntensity(dto.getPreferredIntensity())
                .recoveryCondition(dto.getRecoveryCondition())
                .preferredEnvironment(dto.getPreferredEnvironment())
                .timeRange(dto.getTimeRange())
                .build();

        // AvoidFactors (String List -> Entity List) 변환 및 연관관계 설정
        if (dto.getAvoidFactors() != null) {
            List<AvoidFactor> factors = dto.getAvoidFactors().stream()
                    .map(factorName -> AvoidFactor.builder()
                            .factorName(factorName)
                            .survey(survey) // 연관관계 설정
                            .build())
                    .collect(Collectors.toList());
            survey.avoidFactors.addAll(factors);
        }

        // InterestedSports (ID List -> Entity List) 변환 및 연관관계 설정
        if (sports != null) {
            List<InterestedSport> interestedSportsList = sports.stream()
                    .map(sport -> InterestedSport.builder()
                            .sport(sport)   // Sport 엔티티를 InterestedSport에 연결
                            .survey(survey) // 연관관계 설정
                            .build())
                    .collect(Collectors.toList());
            survey.interestedSports.addAll(interestedSportsList);
        }

        return survey;
    }
}
