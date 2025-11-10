package com.api.mov.domain.facility.service;

import com.api.mov.domain.facility.entity.Facility;
import com.api.mov.domain.facility.entity.Review;
import com.api.mov.domain.facility.repository.FacilityRepository;
import com.api.mov.domain.facility.repository.ReviewRepository;
import com.api.mov.domain.facility.web.dto.CreateReviewReq;
import com.api.mov.domain.facility.web.dto.FacilityDetailRes;
import com.api.mov.domain.facility.web.dto.FacilityRes;
import com.api.mov.domain.facility.web.dto.GetReviewRes;
import com.api.mov.domain.pass.entity.Sport;
import com.api.mov.domain.pass.entity.UserPass;
import com.api.mov.domain.pass.entity.UserPassStatus;
import com.api.mov.domain.pass.repository.SportRepository;
import com.api.mov.domain.pass.repository.UserPassRepository;
import com.api.mov.domain.user.entity.User;
import com.api.mov.domain.user.repository.UserRepository;
import com.api.mov.global.exception.CustomException;
import com.api.mov.global.response.code.facility.FacilityErrorResponseCode;
import com.api.mov.global.response.code.review.ReviewErrorResponseCode;
import com.api.mov.global.response.code.sport.SportErrorResponseCode;
import com.api.mov.global.response.code.user.UserErrorResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityServiceImpl implements FacilityService {

    private final FacilityRepository facilityRepository;
    private final SportRepository sportRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final UserPassRepository userPassRepository;

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
            throw new CustomException(FacilityErrorResponseCode.NOT_FOUND_SEARCH_RESULTS_404);
        }
        return facilityList.map(this::toFacilityRes);
    }

    @Override
    public FacilityDetailRes getFacilityDetail(Long facilityId) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(()-> new CustomException(FacilityErrorResponseCode.NOT_FOUND_FACILITY_404));

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

    @Override
    @Transactional
    public void createReview(Long facilityId, Long userId, CreateReviewReq createReviewReq) {
        // 사용자 검증
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorResponseCode.USER_NOT_FOUND_404));
        // 시설 검증
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new CustomException(FacilityErrorResponseCode.NOT_FOUND_FACILITY_404));

        // 사용자가 이 시설을 완료했는지 검증
        boolean hasCompleted = hasUserCompletedPackageAtFacility(userId, facilityId);
        if (!hasCompleted) {
            throw new CustomException(ReviewErrorResponseCode.PASS_NOT_COMPLETED);
        }

        // 중복 후기 작성 방지
        if (reviewRepository.existsByUserIdAndFacilityId(userId, facilityId)) {
            throw new CustomException(ReviewErrorResponseCode.REVIEW_ALREADY_EXISTS);
        }

        Review review = Review.builder()
                .user(user)
                .facility(facility)
                .comment(createReviewReq.getComment())
                .build();
        reviewRepository.save(review);

    }

    @Override
    @Transactional(readOnly = true) // [추가] getReviews 메소드 구현
    public Page<GetReviewRes> getReviews(Long facilityId, Pageable pageable) {
        // 시설이 존재하는지 확인
        if (!facilityRepository.existsById(facilityId)) {
            throw new CustomException(FacilityErrorResponseCode.NOT_FOUND_FACILITY_404);
        }

        // 레포지토리에서 리뷰 페이징 조회
        Page<Review> reviewPage = reviewRepository.findAllByFacilityId(facilityId, pageable);

        // Page<Review>를 Page<GetReviewRes>로 변환
        return reviewPage.map(GetReviewRes::new); // GetReviewRes 생성자(Review entity) 호출
    }
    private FacilityRes toFacilityRes(Facility facility) {
        return new FacilityRes(
                facility.getId(),
                facility.getName(),
                facility.getAddress(),
                facility.getPrice() // Facility 엔티티에 price 필드가 있다고 가정
        );
    }

    private boolean hasUserCompletedPackageAtFacility(Long userId, Long facilityId) {
        // 유저의 'COMPLETED' 상태인 UserPass 목록을 가져옴
        List<UserPass> completedPasses = userPassRepository.findByUserIdAndStatusWithPassDetails(userId, UserPassStatus.COMPLETED);

        if (completedPasses.isEmpty()) {
            return false;
        }

        // 이 패키지들 중 하나라도 해당 facilityId를 포함하는지 확인
        return completedPasses.stream()
                .map(userPass -> userPass.getPass().getPassItems()) // List<List<PassItem>>
                .flatMap(List::stream) // Stream<PassItem>
                .map(passItem -> passItem.getFacility().getId()) // Stream<Long> (시설 ID)
                .anyMatch(id -> id.equals(facilityId));
    }
}

