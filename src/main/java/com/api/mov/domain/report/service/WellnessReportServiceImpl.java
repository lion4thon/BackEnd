package com.api.mov.domain.report.service;

import com.api.mov.domain.pass.entity.Sport;
import com.api.mov.domain.pass.repository.SportRepository;
import com.api.mov.domain.report.entity.WellnessReport;
import com.api.mov.domain.report.repository.WellnessReportRepository;
import com.api.mov.domain.report.web.dto.CreateWellnessReportReq;
import com.api.mov.domain.report.web.dto.CreateWellnessReportRes;
import com.api.mov.domain.report.web.dto.GenerateReportRes;
import com.api.mov.domain.user.entity.User;
import com.api.mov.domain.user.repository.UserRepository;
import com.api.mov.global.exception.CustomException;
import com.api.mov.global.external.gpt.dto.LlmReportDto;
import com.api.mov.global.external.gpt.service.LlmService;
import com.api.mov.global.response.code.report.ReportErrorResponseCode;
import com.api.mov.global.response.code.sport.SportErrorResponseCode;
import com.api.mov.global.response.code.user.UserErrorResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WellnessReportServiceImpl implements WellnessReportService {

    private final WellnessReportRepository wellnessReportRepository;
    private final UserRepository userRepository;
    private final SportRepository sportRepository;
    private final LlmService llmService;


    @Override
    @Transactional
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
    @Override
    @Transactional
    public GenerateReportRes generateLlmReport(Long reportId) {

        WellnessReport report = wellnessReportRepository.findById(reportId)
                .orElseThrow(() -> new CustomException(ReportErrorResponseCode.NOT_FOUND_REPORT));

        if (report.getReportTitle() != null && !report.getReportTitle().isEmpty()) {
            return new GenerateReportRes(
                    report.getId(),
                    report.getReportTitle(),
                    report.getReportContent(),
                    report.getReportFeedback()
            );
        }

        // 👇 [수정] LlmService가 Mono를 반환하므로, .block()으로 결과를 동기식으로 기다립니다.
        LlmReportDto llmReportDto = llmService.generateReport(report).block();

        if (llmReportDto == null) {
            throw new RuntimeException("LLM 리포트 생성에 실패했습니다.");
        }

        report.setReportTitle(llmReportDto.getTitle());
        report.setReportContent(llmReportDto.getContent());
        report.setReportFeedback(llmReportDto.getFeedback());
        wellnessReportRepository.save(report);

        return new GenerateReportRes(
                report.getId(),
                report.getReportTitle(),
                report.getReportContent(),
                report.getReportFeedback()
        );
    }
}
