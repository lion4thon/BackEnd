package com.api.mov.domain.facility.web.controller;

import com.api.mov.domain.facility.service.FacilityService;
import com.api.mov.domain.facility.web.dto.CreateReviewReq;
import com.api.mov.domain.facility.web.dto.FacilityDetailRes;
import com.api.mov.domain.facility.web.dto.FacilityRes;
import com.api.mov.global.jwt.UserPrincipal;
import com.api.mov.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FacilityController {

    private final FacilityService facilityService;

    //선택한 운동 종목으로 조회
    @GetMapping("/facilities")
    public ResponseEntity<SuccessResponse<?>> getFacilities(@RequestParam String sportName,
                                                            @PageableDefault(size = 4, sort = "id") Pageable pageable) {

        Page<FacilityRes> facilityResPage = facilityService.getFacilityList(sportName, pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(facilityResPage));
    }

    //검색한 업장명으로 조회
    @GetMapping("/facilities/search")
    public ResponseEntity<SuccessResponse<?>> searchFacilities(@RequestParam String query,
                                                               @PageableDefault(size = 4, sort = "id") Pageable pageable) {
        Page<FacilityRes> facilityResPage = facilityService.searchFacilities(query, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(facilityResPage));
    }

    //업장 상세정보 조회
    @GetMapping("/facilities/{facilityId}")
    public ResponseEntity<SuccessResponse<?>> getFacilityDetail(@PathVariable Long facilityId) {
        FacilityDetailRes detail = facilityService.getFacilityDetail(facilityId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(detail));
    }

    // 5. 후기 작성 API 엔드포인트 추가
    @PostMapping("/facilities/{facilityId}/reviews")
    public ResponseEntity<SuccessResponse<?>> createReview(
            @PathVariable Long facilityId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CreateReviewReq createReviewReq) {

        facilityService.createReview(facilityId, userPrincipal.getId(), createReviewReq);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.success("후기 작성을 성공했습니다."));
    }
}
