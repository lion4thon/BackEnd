package com.api.mov.domain.recommendation.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AISurveyRequest {
    private String purpose;                 // 운동 목적
    private String preferred_time;          // 선호 운동 시간
    private String preferred_intensity;     // 선호 운동 강도
    private String travel_time;             // 이동 가능 시간
    private String environment;             // 운동 환경 (실내/실외)
    private List<String> preferred_sports;  // 관심 운동 종목
    private String recovery_level;          // 회복 정도
    private String budget_range;            // 1회 기준 패키지 예산
    private List<String> avoid_factors;     // 피하고 싶은 요소
}
