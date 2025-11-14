package com.api.mov.domain.recommendation.service;

import com.api.mov.domain.pass.entity.Pass;
import com.api.mov.domain.pass.repository.PassRepository;
import com.api.mov.domain.recommendation.web.dto.AIRecommendationListRes;
import com.api.mov.domain.recommendation.web.dto.AIRecommendationRes;
import com.api.mov.domain.recommendation.web.dto.AISurveyRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIRecommendationService {

    private final RestTemplate restTemplate;
    private final PassRepository passRepository;

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

            // Pass 데이터 조회해서 imageUrl 추가
            if (response != null && response.getRecommendations() != null) {
                enrichWithImageUrls(response.getRecommendations());
            }

            return response;

        } catch (Exception e) {
            log.error("FastAPI 추천 요청 실패: {}", e.getMessage(), e);
            throw new RuntimeException("AI 추천 서비스 호출 실패: " + e.getMessage());
        }
    }

    /**
     * AI 추천 결과에 Pass의 imageUrl 추가
     */
    private void enrichWithImageUrls(List<AIRecommendationRes> recommendations) {
        // Pass ID 목록 추출
        List<Long> passIds = recommendations.stream()
                .map(AIRecommendationRes::getPassId)
                .collect(Collectors.toList());

        // Pass 데이터 조회
        List<Pass> passes = passRepository.findAllById(passIds);

        // Pass ID -> imageUrl 매핑
        Map<Long, String> imageUrlMap = passes.stream()
                .collect(Collectors.toMap(
                        Pass::getId,
                        pass -> pass.getImageUrl() != null ? pass.getImageUrl() : ""
                ));

        // 각 추천 결과에 imageUrl 설정
        for (AIRecommendationRes recommendation : recommendations) {
            String imageUrl = imageUrlMap.getOrDefault(recommendation.getPassId(), "");
            recommendation.setImageUrl(imageUrl);
        }

        log.info("추천 결과에 이미지 URL 추가 완료: {} 개", recommendations.size());
    }
}
