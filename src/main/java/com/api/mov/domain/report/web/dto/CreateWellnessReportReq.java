package com.api.mov.domain.report.web.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateWellnessReportReq {

    @NotNull(message = "패키지 ID는 필수입니다.")
    private Long passId; // 패키지 ID

    @NotEmpty(message = "운동 강도를 선택해주세요.")
    private String workoutIntensity;

    @NotEmpty(message = "운동 후 컨디션을 선택해주세요.")
    private String postWorkoutCondition;

    private List<String> muscleActivationAreas;

    private List<String> postWorkoutMood;

    private String oneLineNote;}
