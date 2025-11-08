package com.api.mov.domain.survey.web.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSurveyReq {

    @NotNull(message = "사용자 ID는 필수입니다.")
    private Long userId; // user_id

    @NotEmpty(message = "운동 목적은 필수입니다.")
    private String purpose; // 운동 목적

    @NotEmpty(message = "운동 선호 시간은 필수입니다.")
    private String preferredTime; // 운동 선호 시간

    @NotNull(message = "1회 기준 패키지 적정 가격은 필수입니다.")
    private Long price; // 1회 기준 패키지 적정 가격

    @NotEmpty(message = "운동 선호 강도는 필수입니다.")
    private String preferredIntensity; // 운동 선호 강도

    @NotEmpty(message = "운동 후 회복 정도는 필수입니다.")
    private String recoveryCondition; // 운동 후 회복 정도

    @NotEmpty(message = "선호하는 운동 환경은 필수입니다.")
    private String preferredEnvironment; // 선호하는 운동 환경

    @NotEmpty(message = "이동 가능 시간은 필수입니다.")
    private String timeRange; // 이동 가능 시간

    // 다중 응답: 피하고 싶은 요소 (문자열 목록)
    private List<String> avoidFactors; // 예: ["살짝 땀이 나는 정도", "가벼운 웜업 위주"]

    // 다중 응답: 관심있는 운동 종목 (ID 목록)
    private List<Long> interestedSportIds; // 예: [1, 3, 5] (sport_id)
}
