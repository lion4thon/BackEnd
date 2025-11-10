package com.api.mov.domain.facility.service;

import com.api.mov.domain.facility.entity.Facility;
import com.api.mov.domain.facility.web.dto.CreateReviewReq;
import com.api.mov.domain.facility.web.dto.FacilityDetailRes;
import com.api.mov.domain.facility.web.dto.FacilityRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FacilityService {
    Page<FacilityRes> getFacilityList(String sportName, Pageable pageable);
    Page<FacilityRes> searchFacilities(String query, Pageable pageable);
    FacilityDetailRes getFacilityDetail(Long facilityId);

    // 후기 작성메소드 추가
    void createReview(Long facilityId, Long userId, CreateReviewReq createReviewReq);
}
