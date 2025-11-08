package com.api.mov.domain.report.service;

import com.api.mov.domain.report.web.dto.CreateWellnessReportReq;
import com.api.mov.domain.report.web.dto.CreateWellnessReportRes;
import com.api.mov.domain.report.web.dto.GenerateReportRes;
import com.api.mov.domain.report.web.dto.GetWellnessReportRes;

public interface WellnessReportService {
    // 감각 리포트 생성 기반 설문 생성
    CreateWellnessReportRes createReport(CreateWellnessReportReq createWellnessReportReq, Long userId);
    // 감각 리포트 생성
    GenerateReportRes generateLlmReport(Long reportId, Long userId);
    // 감각 리포트 조회
    GetWellnessReportRes getReport(Long reportId,Long userId);
}
