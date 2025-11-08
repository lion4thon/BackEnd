package com.api.mov.domain.report.service;

import com.api.mov.domain.pass.entity.Sport;
import com.api.mov.domain.pass.repository.SportRepository;
import com.api.mov.domain.report.entity.WellnessReport;
import com.api.mov.domain.report.repository.WellnessReportRepository;
import com.api.mov.domain.report.web.dto.CreateWellnessReportReq;
import com.api.mov.domain.report.web.dto.CreateWellnessReportRes;
import com.api.mov.domain.user.entity.User;
import com.api.mov.domain.user.repository.UserRepository;
import com.api.mov.global.exception.CustomException;
import com.api.mov.global.response.code.sport.SportErrorResponseCode;
import com.api.mov.global.response.code.user.UserErrorResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WellnessReportServiceImpl implements WellnessReportService {

    private final WellnessReportRepository wellnessReportRepository;
    private final UserRepository userRepository;
    private final SportRepository sportRepository;


    @Override
    public CreateWellnessReportRes createReport(CreateWellnessReportReq createWellnessReportReq) {
        // 사용자 조회
        User user = userRepository.findById(createWellnessReportReq.getUserId())
                .orElseThrow(() -> new CustomException(UserErrorResponseCode.NOT_FOUND_USER_404));

        // 운동 종목 조회
        Sport sport = sportRepository.findById(createWellnessReportReq.getSportId())
                .orElseThrow(() -> new CustomException(SportErrorResponseCode.NOT_FOUND_SPORT));

        // TODO : 패키지 완료 여부 검증 로직


        // 반환
        WellnessReport wellnessReport = WellnessReport.builder()
                .user(user)
                .sport(sport)
                .workoutIntensity(createWellnessReportReq.getWorkoutIntensity())
                .postWorkoutCondition(createWellnessReportReq.getPostWorkoutCondition())
                .muscleActivationAreas(createWellnessReportReq.getMuscleActivationAreas())
                .postWorkoutMood(createWellnessReportReq.getPostWorkoutMood())
                .oneLineNote(createWellnessReportReq.getOneLineNote())
                .build();

        WellnessReport savedReport = wellnessReportRepository.save(wellnessReport);

        return new CreateWellnessReportRes(
                savedReport.getId(),
                savedReport.getUser().getId(),
                savedReport.getSport().getId(),
                savedReport.getWorkoutIntensity(),
                savedReport.getPostWorkoutCondition(),
                savedReport.getMuscleActivationAreas(),
                savedReport.getPostWorkoutMood(),
                savedReport.getOneLineNote()
        );

    }
}
