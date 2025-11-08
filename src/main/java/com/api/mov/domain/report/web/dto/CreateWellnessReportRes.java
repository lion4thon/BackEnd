package com.api.mov.domain.report.web.dto;

import java.util.List;

public record CreateWellnessReportRes(
        Long reportId,
        Long userId,
        Long sportId,
        String workoutIntensity,
        String postWorkoutCondition,
        List<String> muscleActivationAreas,
        List<String> postWorkoutMood,
        String oneLineNote
) {
}
