package com.api.mov.global.external.gpt.service;

import com.api.mov.domain.report.entity.WellnessReport;
import com.api.mov.global.external.gpt.dto.LlmReportDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class OpenAiLlmServiceImpl implements LlmService {

    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${openai.api.key}")
    private String openaiApiKey;

    @Value("${openai.api.url}")
    private String openaiApiUrl;

    @Override
    public Mono<LlmReportDto> generateReport(WellnessReport report) {
        String prompt = buildPrompt(report);

        // 주입받은 Builder로 WebClient 생성
        WebClient webClient = webClientBuilder
                .baseUrl(openaiApiUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openaiApiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        // API 요청 본문(Body) 생성
        Map<String, Object> body = Map.of(
                "model", "gpt-4o",
                "response_format", Map.of("type", "json_object"),
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a helpful wellness coach. Respond in Korean. You must output valid JSON."),
                        Map.of("role", "user", "content", prompt)
                )
        );

        // WebClient API 호출
        return webClient.post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(JsonNode.class) // 1차로 JsonNode로 받음
                .flatMap(jsonNode -> {
                    try {
                        // 2차로 응답 content의 JSON 문자열을 LlmReportDto로 파싱
                        String jsonString = jsonNode.path("choices").get(0).path("message").path("content").asText();
                        LlmReportDto dto = objectMapper.readValue(jsonString, LlmReportDto.class);
                        return Mono.just(dto);
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("LLM JSON 파싱에 실패했습니다.", e));
                    }
                });    }
    private String buildPrompt(WellnessReport report) {
        String areas = String.join(", ", report.getMuscleActivationAreas());
        String moods = String.join(", ", report.getPostWorkoutMood());

        return String.format(
                "다음은 사용자의 운동 후 감각 리포트 데이터입니다:\n" +
                        "- 운동 강도: %s\n" +
                        "- 운동 후 컨디션: %s\n" +
                        "- 주요 자극 부위: %s\n" +
                        "- 운동 후 기분: %s\n" +
                        "- 한 줄 기록: %s\n" +
                        "\n" +
                        "이 데이터를 바탕으로 사용자를 위한 웰니스 리포트를 JSON 형식으로 생성해 주세요. " +
                        "JSON 객체는 반드시 'title', 'content', 'feedback' 세 개의 키를 가져야 합니다.\n" +
                        "1. 'title': 데이터를 요약하는 매력적인 리포트 제목\n" +
                        "2. 'content': 데이터를 분석하고 사용자를 격려하는 리포트 본문 (4~5문장)\n" +
                        "3. 'feedback': 사용자의 상태에 기반한 간단한 피드백이나 다음 운동을 위한 팁 (1~2문장)",
                report.getWorkoutIntensity(),
                report.getPostWorkoutCondition(),
                areas,
                moods,
                report.getOneLineNote()
        );
    }

}
