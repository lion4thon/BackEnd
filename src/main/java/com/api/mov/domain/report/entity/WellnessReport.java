package com.api.mov.domain.report.entity;

import com.api.mov.domain.pass.entity.Sport;
import com.api.mov.domain.user.entity.User;
import com.api.mov.global.coverter.SimpleStringListConverter;
import com.api.mov.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class WellnessReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "workout_intensity")
    private String workoutIntensity; // 운동 강도

    @Column(name = "post_workout_condition")
    private String postWorkoutCondition; // 운동 후 컨디션

    @Column(name = "one_line_note", columnDefinition = "TEXT")
    private String oneLineNote; // 한 줄 기록

    // DB에 저장될 때는 SimpleStringListConverter가 변환
    @Convert(converter = SimpleStringListConverter.class)
    @Column(name = "muscle_activation_areas") // ERD의 VARCHAR 타입
    private List<String> muscleActivationAreas; // 자극이 느껴진 부위

    @Convert(converter = SimpleStringListConverter.class)
    @Column(name = "post_workout_mood") // ERD의 VARCHAR 타입
    private List<String> postWorkoutMood; // 운동 후 기분이나 에너지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sport_id", nullable = false)
    private Sport sport;

    @Column(name = "report_title", columnDefinition = "TEXT")
    private String reportTitle; // 리포트 제목

    @Column(name = "report_content", columnDefinition = "TEXT")
    private String reportContent; // 리포트 내용

    @Column(name = "report_feedback", columnDefinition = "TEXT")
    private String reportFeedback; // 피드백 & 코멘트
}
