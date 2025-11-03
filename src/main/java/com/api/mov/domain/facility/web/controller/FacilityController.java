package com.api.mov.domain.facility.web.controller;

import com.api.mov.domain.facility.service.FacilityService;
import com.api.mov.domain.facility.web.dto.FacilityDetailRes;
import com.api.mov.domain.facility.web.dto.FacilityRes;
import com.api.mov.global.response.SuccessResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FacilityController {

    private final FacilityService facilityService;

    @GetMapping("/facilities")
    public ResponseEntity<SuccessResponse<?>> getFacilities(@RequestParam String sportName,
                                                            @PageableDefault(size = 4, sort = "id") Pageable pageable) {

        Page<FacilityRes> facilityResPage = facilityService.getFacilityList(sportName, pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(facilityResPage));
    }

    @GetMapping("/facilities/{facilityId}")
    public ResponseEntity<SuccessResponse<?>> getFacility(@PathVariable Long facilityId) {
        FacilityDetailRes detail = facilityService.getFacilityDetail(facilityId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(detail));
    }
}
