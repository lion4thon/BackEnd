package com.api.mov.domain.recommendation.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIRecommendationListRes {
    private List<AIRecommendationRes> recommendations;

    @JsonProperty("total_count")
    private Integer totalCount;
}
