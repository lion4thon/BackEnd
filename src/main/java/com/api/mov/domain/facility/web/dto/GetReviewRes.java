package com.api.mov.domain.facility.web.dto;

import com.api.mov.domain.facility.entity.Review;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
public class GetReviewRes {
    private Long reviewId;
    private String username;
    private int rating; // Review 엔티티에 rating 필드가 있습니다.
    private String comment;
    private String createdAt;

    // Entity를 DTO로 변환하는 생성자
    public GetReviewRes(Review review) {
        this.reviewId = review.getId();
        this.username = review.getUser().getUsername(); // User 엔티티에서 사용자 이름을 가져옵니다.
        this.rating = review.getRating();
        this.comment = review.getComment();
        // 날짜 형식을 "2025.11.13"처럼 맞춥니다.
        this.createdAt = review.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }
}
