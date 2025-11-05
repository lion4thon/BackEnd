package com.api.mov.domain.facility.service;

import com.api.mov.domain.facility.entity.Facility;
import com.api.mov.domain.facility.repository.FacilityRepository;
import com.api.mov.domain.facility.web.dto.FacilityDetailRes;
import com.api.mov.domain.facility.web.dto.FacilityRes;
import com.api.mov.domain.pass.entity.Sport;
import com.api.mov.domain.pass.repository.SportRepository;
import com.api.mov.global.exception.CustomException;
import com.api.mov.global.response.code.facility.FacilityErrorResponseCode;
import com.api.mov.global.response.code.sport.SportErrorResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FacilityServiceImpl implements FacilityService {

    private final FacilityRepository facilityRepository;
    private final SportRepository sportRepository;

    @Override
    public Page<FacilityRes> getFacilityList(String sportName, Pageable pageable) {

        Sport sport = sportRepository.findByNameIgnoreCase(sportName)
                .orElseThrow(() -> new CustomException(SportErrorResponseCode.NOT_FOUND_SPORT));
        
        Page<Facility> facilityList = facilityRepository.findBySportId(sport.getId(), pageable);
        
        return facilityList.map(this::toFacilityRes);
    }

    @Override
    public Page<FacilityRes> searchFacilities(String query, Pageable pageable) {
        Page<Facility> facilityList = facilityRepository.findByNameContaining(query, pageable);
        if(facilityList.isEmpty()){
            throw new CustomException(FacilityErrorResponseCode.NOT_FOUND_SEARCH_RESULTS);
        }
        return facilityList.map(this::toFacilityRes);
    }

    @Override
    public FacilityDetailRes getFacilityDetail(Long facilityId) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(()-> new CustomException(FacilityErrorResponseCode.NOT_FOUND_FACILITY));

        return new FacilityDetailRes(
                facility.getId(),
                facility.getName(),
                facility.getAddress(),
                facility.getDetailAddress(),
                facility.getContact(),
                facility.getPrice(),
                facility.getFeatures(),
                facility.getWeekdayHours(),
                facility.getWeekendHours(),
                facility.getHolidayClosedInfo(),
                facility.getAccessGuide()
        );
    }

    private FacilityRes toFacilityRes(Facility facility) {
        return new FacilityRes(
                facility.getId(),
                facility.getName(),
                facility.getAddress(),
                facility.getPrice() // Facility 엔티티에 price 필드가 있다고 가정
        );
    }
}
