package com.api.mov.domain.report.web.controller;

import com.api.mov.domain.report.service.WellnessReportService;
import com.api.mov.domain.report.web.dto.CreateWellnessReportReq;
import com.api.mov.domain.report.web.dto.CreateWellnessReportRes;
import com.api.mov.global.response.SuccessResponse;
import com.sun.net.httpserver.Authenticator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
