package com.api.mov.domain.report.web.dto;

public record GetWellnessReportRes(
        Long reportId,
        Long userId,
        String passName, // h무슨 패키지 리포트인지
        String reportTitle,
        String reportContent,
        String reportFeedback
) {

}
