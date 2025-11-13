package com.api.mov.domain.recommendation.web.controller;

import com.api.mov.domain.recommendation.web.dto.AIRecommendationListRes;
import com.api.mov.domain.recommendation.web.dto.AISurveyRequest;
import com.api.mov.domain.recommendation.service.AIRecommendationService;
import com.api.mov.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AIRecommendationController {

    private final AIRecommendationService aiRecommendationService;

    /**
     * AI 기반 패키지 추천 API
     */
    @PostMapping("/recommendations")
    public ResponseEntity<SuccessResponse<?>> getAIRecommendations(
            @RequestBody AISurveyRequest surveyRequest
    ) {
        log.info("AI 추천 요청 수신: purpose={}, intensity={}",
                surveyRequest.getPurpose(),
                surveyRequest.getPreferred_intensity());

        AIRecommendationListRes recommendations = aiRecommendationService.getRecommendations(surveyRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(recommendations));
    }
}
