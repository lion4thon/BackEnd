package com.api.mov.domain.survey.web.controller;

import com.api.mov.domain.survey.service.SurveyService;
import com.api.mov.domain.survey.web.dto.CreateSurveyReq;
import com.api.mov.domain.survey.web.dto.CreateSurveyRes;
import com.api.mov.global.jwt.UserPrincipal;
import com.api.mov.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/survey")
public class SurveyController {

    private final SurveyService surveyService;

    @PostMapping
    public ResponseEntity<SuccessResponse<?>> createSurvey(@Valid @RequestBody CreateSurveyReq createSurveyReq, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        CreateSurveyRes createSurvey = surveyService.createSurvey(createSurveyReq,userPrincipal.getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.ok(createSurvey));
    }
}
