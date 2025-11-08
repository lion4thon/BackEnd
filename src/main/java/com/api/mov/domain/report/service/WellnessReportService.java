package com.api.mov.domain.report.service;

import com.api.mov.domain.report.web.dto.CreateWellnessReportReq;
import com.api.mov.domain.report.web.dto.CreateWellnessReportRes;
import com.api.mov.domain.report.web.dto.GenerateReportRes;

public interface WellnessReportService {
    // 감각 리포트 생성 기반 설문 생성
    CreateWellnessReportRes createReport(CreateWellnessReportReq createWellnessReportReq);
    // 감각 리포트 생성
    GenerateReportRes generateLlmReport(Long reportId);
}
