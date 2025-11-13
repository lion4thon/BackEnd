package com.api.mov.domain.recommendation.service;

import com.api.mov.domain.recommendation.web.dto.AIRecommendationListRes;
import com.api.mov.domain.recommendation.web.dto.AISurveyRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIRecommendationService {

    private final RestTemplate restTemplate;

    @Value("${fastapi.url:http://localhost:8000}")
    private String fastApiUrl;

    public AIRecommendationListRes getRecommendations(AISurveyRequest surveyRequest) {
        try {
            String url = fastApiUrl + "/api/recommendations";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<AISurveyRequest> request = new HttpEntity<>(surveyRequest, headers);

            log.info("FastAPI 추천 요청 전송: {}", url);

            AIRecommendationListRes response = restTemplate.postForObject(
                    url,
                    request,
                    AIRecommendationListRes.class
            );

            log.info("FastAPI 추천 결과 수신: {} 개", response != null ? response.getTotalCount() : 0);

            return response;

        } catch (Exception e) {
            log.error("FastAPI 추천 요청 실패: {}", e.getMessage(), e);
            throw new RuntimeException("AI 추천 서비스 호출 실패: " + e.getMessage());
        }
    }
}
