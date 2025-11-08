package com.api.mov.domain.report.web.dto;

public record GenerateReportRes(
        Long reportId,
        String title,
        String content,
        String feedback
) {
}
