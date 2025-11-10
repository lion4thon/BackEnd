package com.api.mov.domain.facility.repository;

import com.api.mov.domain.facility.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 중복 리뷰 방지를 위한 쿼리
    boolean existsByUserIdAndFacilityId(Long userId, Long facilityId);
}
