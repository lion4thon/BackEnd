package com.api.mov.domain.survey.web.dto;

import com.api.mov.domain.survey.entity.AvoidFactor;
import com.api.mov.domain.survey.entity.InterestedSport;
import com.api.mov.domain.survey.entity.Survey;

import java.util.List;
import java.util.stream.Collectors;

public record CreateSurveyRes(
        Long surveyId,
        Long userId,
        String purpose,
        String preferredTime,
        Long price,
        String preferredIntensity,
        String recoveryCondition,
        String timeRange,
        List<String> avoidFactors,
        List<Long> interestedSportIds

) {

    /**
     * 💡 [오류 수정] 이 정적 팩토리 메소드가 누락되었습니다.
     * Survey 엔티티를 CreateSurveyRes DTO(Record)로 변환합니다.
     */
    public static CreateSurveyRes from(Survey survey) {

        // 1. 자식 엔티티(List<AvoidFactor>) -> 단순 데이터(List<String>)로 변환
        List<String> factorNames = survey.getAvoidFactors().stream()
                .map(AvoidFactor::getFactorName)
                .collect(Collectors.toList());

        // 2. 자식 엔티티(List<InterestedSport>) -> 단순 데이터(List<Long>)로 변환
        List<Long> sportIds = survey.getInterestedSports().stream()
                .map(InterestedSport::getSportId) // InterestedSport 엔티티에 getSportId() 필요
                .collect(Collectors.toList());

        // 3. DTO(Record) 생성하여 반환
        return new CreateSurveyRes(
                survey.getId(),
                survey.getUser().getId(), // User 객체에서 ID를 가져옵니다.
                survey.getPurpose(),
                survey.getPreferredTime(),
                survey.getPrice(),
                survey.getPreferredIntensity(),
                survey.getRecoveryCondition(),
                survey.getTimeRange(),
                factorNames,  // 변환된 리스트
                sportIds      // 변환된 리스트
        );
}
