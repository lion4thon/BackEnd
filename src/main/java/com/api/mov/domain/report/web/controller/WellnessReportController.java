package com.api.mov.domain.report.web.controller;

import com.api.mov.domain.report.service.WellnessReportService;
import com.api.mov.domain.report.web.dto.CreateWellnessReportReq;
import com.api.mov.domain.report.web.dto.CreateWellnessReportRes;
import com.api.mov.domain.report.web.dto.GenerateReportRes;
import com.api.mov.global.response.SuccessResponse;
import com.sun.net.httpserver.Authenticator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class WellnessReportController {

    private final WellnessReportService wellnessReportService;

    @PostMapping
    public ResponseEntity<SuccessResponse<?>> createReport(@Valid @RequestBody CreateWellnessReportReq createReportReq){
        CreateWellnessReportRes createReportRes = wellnessReportService.createReport(createReportReq);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.success(createReportRes));
    }


//  LLM 리포트 생성 요청 API
    @PostMapping("/{reportId}/generate")
    public ResponseEntity<SuccessResponse<?>> generateLlmReport(@PathVariable Long reportId) {

        GenerateReportRes generateReportRes = wellnessReportService.generateLlmReport(reportId);

        // 생성된결과를 200 OK로 반환
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(generateReportRes));
    }
}
