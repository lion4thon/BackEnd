package com.api.mov.domain.survey.service;

import com.api.mov.domain.pass.entity.Sport;
import com.api.mov.domain.pass.repository.SportRepository;
import com.api.mov.domain.survey.entity.Survey;
import com.api.mov.domain.survey.repository.SurveyRepository;
import com.api.mov.domain.survey.web.dto.CreateSurveyReq;
import com.api.mov.domain.survey.web.dto.CreateSurveyRes;
import com.api.mov.domain.user.repository.UserRepository;
import com.api.mov.global.exception.CustomException;
import com.api.mov.global.response.code.sport.SportErrorResponseCode;
import com.api.mov.global.response.code.user.UserErrorResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyServiceImpl implements SurveyService {

    private final SurveyRepository surveyRepository;
    private final UserRepository userRepository;
    private final SportRepository sportRepository;

    @Override
    @Transactional
    public CreateSurveyRes createSurvey(CreateSurveyReq createSurveyReq) {
        userRepository.findById(createSurveyReq.getUserId())
                // 예외 커스텀 예정
                .orElseThrow(() -> new CustomException(UserErrorResponseCode.NOT_FOUND_USER_404));

        // sportId 검증로직 추가 예정
        List<Sport> sportEntities = null;
        if (createSurveyReq.getInterestedSportIds() != null) {
            sportEntities = createSurveyReq.getInterestedSportIds().stream()
                    .map(sportId -> sportRepository.findById(sportId)
                            .orElseThrow(() -> new CustomException(SportErrorResponseCode.NOT_FOUND_SPORT)))
                    .collect(Collectors.toList());
        }

        // DTO 엔티티 변환
        Survey survey = Survey.createFromDto(createSurveyReq);
        // 설문 DB 저장
        Survey savedSurvey = surveyRepository.save(survey);

        return CreateSurveyRes.from(savedSurvey);

    }
}
