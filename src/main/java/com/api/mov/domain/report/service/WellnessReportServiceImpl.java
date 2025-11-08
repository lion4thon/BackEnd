package com.api.mov.domain.report.service;

import com.api.mov.domain.pass.entity.Pass;
import com.api.mov.domain.pass.repository.PassRepository;
import com.api.mov.domain.report.entity.WellnessReport;
import com.api.mov.domain.report.repository.WellnessReportRepository;
import com.api.mov.domain.report.web.dto.CreateWellnessReportReq;
import com.api.mov.domain.report.web.dto.CreateWellnessReportRes;
import com.api.mov.domain.report.web.dto.GenerateReportRes;
import com.api.mov.domain.report.web.dto.GetWellnessReportRes;
import com.api.mov.domain.user.entity.User;
import com.api.mov.domain.user.repository.UserRepository;
import com.api.mov.global.exception.CustomException;
import com.api.mov.global.external.gpt.dto.LlmReportDto;
import com.api.mov.global.external.gpt.service.LlmService;
import com.api.mov.global.response.code.report.ReportErrorResponseCode;
import com.api.mov.global.response.code.user.UserErrorResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WellnessReportServiceImpl implements WellnessReportService {

    private final WellnessReportRepository wellnessReportRepository;
    private final UserRepository userRepository;
    private final LlmService llmService;
    private final PassRepository passrepository;

    @Override
    @Transactional
    public CreateWellnessReportRes createReport(CreateWellnessReportReq createWellnessReportReq,Long userID) {

        User user = userRepository.findById(userID)
                .orElseThrow(() -> new CustomException(UserErrorResponseCode.USER_NOT_FOUND_404));

        // pass 종목 조회
        Pass pass = passrepository.findById(createWellnessReportReq.getPassId())
                .orElseThrow(() -> new CustomException(ReportErrorResponseCode.NOT_FOUND_PASS));

        // TODO : 패키지 완료 여부 검증 로직

        // 반환
        WellnessReport wellnessReport = WellnessReport.builder()
                .user(user)
                .pass(pass)
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
                savedReport.getPass().getId(),
                savedReport.getWorkoutIntensity(),
                savedReport.getPostWorkoutCondition(),
                savedReport.getMuscleActivationAreas(),
                savedReport.getPostWorkoutMood(),
                savedReport.getOneLineNote()
        );

    }
    @Override
    @Transactional
    public GenerateReportRes generateLlmReport(Long reportId, Long userId) {

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

        LlmReportDto llmReportDto = llmService.generateReport(report).block();

        if (llmReportDto == null) {
            throw new RuntimeException("LLM 리포트 생성에 실패했습니다.");
        }

        report.setReportTitle(llmReportDto.getTitle());
        report.setReportContent(llmReportDto.getContent());
        report.setReportFeedback(llmReportDto.getFeedback());

        return new GenerateReportRes(
                report.getId(),
                report.getReportTitle(),
                report.getReportContent(),
                report.getReportFeedback()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public GetWellnessReportRes getReport(Long reportId,Long userId) {

        WellnessReport report = wellnessReportRepository.findById(reportId)
                .orElseThrow(() -> new CustomException(ReportErrorResponseCode.NOT_FOUND_REPORT));

        if (!report.getUser().getId().equals(userId)) {
            throw new CustomException(ReportErrorResponseCode.NO_PERMISSION);
        }

        return new GetWellnessReportRes(
                report.getId(),
                report.getUser().getId(),
                report.getPass().getName(),
                report.getReportTitle(),
                report.getReportContent(),
                report.getReportFeedback()
        );
    }
}
