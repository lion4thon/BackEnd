package com.api.mov.domain.survey.service;


import com.api.mov.domain.survey.web.dto.CreateSurveyReq;
import com.api.mov.domain.survey.web.dto.CreateSurveyRes;

public interface SurveyService {
    // 설문 생성
    CreateSurveyRes createSurvey(CreateSurveyReq createSurveyReq, Long userId);
}
