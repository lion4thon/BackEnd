package com.api.mov.domain.report.service;

import com.api.mov.domain.report.web.dto.CreateWellnessReportReq;
import com.api.mov.domain.report.web.dto.CreateWellnessReportRes;

public interface WellnessReportService {
    // 감각 리포트 생성
    CreateWellnessReportRes createReport(CreateWellnessReportReq createWellnessReportReq);
}
