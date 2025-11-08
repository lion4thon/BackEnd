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
                        Map.of("role", "system", "content", "You are an expert fitness coach and wellness data analyst. " +
                                "Your task is to provide a professional report based on user's post-workout data. " +
                                "Respond in Korean and strictly follow the requested JSON format."),
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
        // List<String>을 콤마로 연결 (값이 없으면 "없음" 처리)
        String areas = (report.getMuscleActivationAreas() == null || report.getMuscleActivationAreas().isEmpty())
                ? "특정 부위 없음"
                : String.join(", ", report.getMuscleActivationAreas());

        String moods = (report.getPostWorkoutMood() == null || report.getPostWorkoutMood().isEmpty())
                ? "특별한 기분 없음"
                : String.join(", ", report.getPostWorkoutMood());

        return String.format(
                "다음은 사용자의 운동 후 감각 리포트 데이터입니다. 이 데이터를 분석하여 트레이너에게 제출할 수 있는 전문적인 리포트를 JSON 형식으로 생성해 주세요.\n\n" +
                        "### 사용자 입력 데이터\n" +
                        "- **운동 종목**: %s\n" +
                        "- 운동 강도: %s\n" +
                        "- 운동 후 컨디션: %s\n" +
                        "- 주요 자극 부위: %s\n" +
                        "- 운동 후 기분: %s\n" +
                        "- 사용자 한 줄 기록: %s\n" +
                        "\n" +
                        "### 생성할 리포트 JSON 형식\n" +
                        "JSON 객체는 반드시 'title', 'content', 'feedback' 세 개의 키를 가져야 합니다.\n\n" +
                        "1.  **title**: 리포트의 핵심을 꿰뚫는 전문적인 제목. (예: '%s 세션 후 근피로도 및 활력 증진 보고')\n" +
                        "2.  **content**: \n" +
                        "    - 입력된 5가지 데이터를 기반으로 사용자의 상태를 객관적으로 분석.\n" +
                        "    - 특히 '%s' 운동의 특성과 '주요 자극 부위' 및 '운동 강도'를 연관 지어 분석.\n" +
                        "    - 2~3문장의 전문적인 코칭 톤으로 작성.\n" +
                        "3.  **feedback**: \n" +
                        "    - '운동 후 컨디션'과 '기분'을 바탕으로 한 긍정적 피드백.\n" +
                        "    - 다음 %s 세션을 위해 사용자가 고려할 수 있는 구체적인 팁이나 권장 사항 1~2가지 제시.",

                // 데이터 매핑
                report.getSport().getName(),
                report.getWorkoutIntensity(),
                report.getPostWorkoutCondition(),
                areas,
                moods,
                report.getOneLineNote(),

                // 예시용 매핑
                report.getSport().getName(), // title 예시
                report.getSport().getName(), // content 분석
                report.getSport().getName()  // feedback 팁
        );
    }

}
