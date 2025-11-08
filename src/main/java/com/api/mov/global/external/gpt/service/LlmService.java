package com.api.mov.global.external.gpt.service;

import com.api.mov.domain.report.entity.WellnessReport;
import com.api.mov.global.external.gpt.dto.LlmReportDto;
import reactor.core.publisher.Mono;

public interface LlmService {
    // WellnessReport 데이터 기반으로 LLM에게 리포트 생성 요청
    Mono<LlmReportDto> generateReport(WellnessReport report);
}
