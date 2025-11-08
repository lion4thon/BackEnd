package com.api.mov.domain.report.web.controller;

import com.api.mov.domain.pass.repository.UserPassRepository;
import com.api.mov.domain.report.service.WellnessReportService;
import com.api.mov.domain.report.web.dto.CreateWellnessReportReq;
import com.api.mov.domain.report.web.dto.CreateWellnessReportRes;
import com.api.mov.domain.report.web.dto.GenerateReportRes;
import com.api.mov.domain.report.web.dto.GetWellnessReportRes;
import com.api.mov.global.jwt.UserPrincipal;
import com.api.mov.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class WellnessReportController {

    private final WellnessReportService wellnessReportService;
    private final UserPassRepository userPassRepository;

    @PostMapping
    public ResponseEntity<SuccessResponse<?>> createReport(@Valid @RequestBody CreateWellnessReportReq createReportReq, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        CreateWellnessReportRes createReportRes = wellnessReportService.createReport(createReportReq, userPrincipal.getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.success(createReportRes));
    }


//  LLM 리포트 생성 요청 API
    @PostMapping("/{reportId}/generate")
    public ResponseEntity<SuccessResponse<?>> generateLlmReport(@PathVariable Long reportId, @AuthenticationPrincipal UserPrincipal userPrincipal) {

        GenerateReportRes generateReportRes = wellnessReportService.generateLlmReport(reportId, userPrincipal.getId());

        // 생성된결과를 200 OK로 반환
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(generateReportRes));
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<SuccessResponse<?>> getReport(@PathVariable Long reportId,@AuthenticationPrincipal UserPrincipal userPrincipal) {

        GetWellnessReportRes getReportRes = wellnessReportService.getReport(reportId, userPrincipal.getId());

        // 조회 성공 200 OK로 반환
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(getReportRes));
    }
}
