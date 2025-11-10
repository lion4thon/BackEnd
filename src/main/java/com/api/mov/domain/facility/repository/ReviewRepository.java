package com.api.mov.domain.facility.repository;

import com.api.mov.domain.facility.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 중복 리뷰 방지를 위한 쿼리
    boolean existsByUserIdAndFacilityId(Long userId, Long facilityId);

    /**
     * 특정 시설(facilityId)에 대한 리뷰 목록을 페이징 처리하여 조회
     *
     * @param facilityId 리뷰를 조회할 시설의 ID
     * @param pageable   클라이언트에서 요청한 페이지 정보 (페이지 번호, 페이지 당 개수, 정렬 기준)
     * @return {@link Page} 객체 - 단순한 리뷰 리스트가 아닌, 페이지네이션(Paging)을 위한
     * 메타데이터(총 페이지 수, 총 리뷰 개수, 현재 페이지 번호 등)를 포함하는 응답 객체입니다.
     * 클라이언트는 이 정보를 바탕으로 '더보기' 또는 '페이지 번호' UI를 구현할 수 있습니다.
     **/
    // List로 받는게 아닌 Page로 받는 이유는 수 많은 리뷰를 나눠서 할 수 있기 때문에 이렇게 많이들 쓴다고 합니당
    Page<Review> findAllByFacilityId(Long facilityId, Pageable pageable);
}
