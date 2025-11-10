package com.api.mov.domain.facility.web.dto;

import lombok.Getter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
public class CreateReviewReq {

//    private Integer rating; // 별점

    @NotBlank(message = "후기 내용은 필수입니다.")
    private String comment;
}
