package com.api.mov.domain.survey.service;

import com.api.mov.domain.survey.entity.Survey;
import com.api.mov.domain.survey.repository.SurveyRepository;
import com.api.mov.domain.survey.web.dto.CreateSurveyReq;
import com.api.mov.domain.survey.web.dto.CreateSurveyRes;
import com.api.mov.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyServiceImpl implements SurveyService {

    private final SurveyRepository surveyRepository;
    private final UserRepository userRepository;
    // 추후에 추가
    // private final SportRepository sportRepository;

    @Override
    @Transactional
    public CreateSurveyRes createSurvey(CreateSurveyReq createSurveyReq) {
        userRepository.findById(createSurveyReq.getUserId())
                // 예외 커스텀 예정
                .orElseThrow(() -> new RuntimeException("User not found"));

        // sportId 검증로직 추가 예정

        // DTO 엔티티 변환
        Survey survey = Survey.createFromDto(createSurveyReq);
        // 설문 DB 저장
        Survey savedSurvey = surveyRepository.save(survey);

        return CreateSurveyRes.from(savedSurvey);

    }
}
