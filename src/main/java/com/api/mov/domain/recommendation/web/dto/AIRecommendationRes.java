package com.api.mov.domain.recommendation.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIRecommendationRes {
    @JsonProperty("pass_id")
    private Long passId;

    private String name;
    private Integer price;
    private String intensity;

    @JsonProperty("purposeTag")
    private String purposeTag;

    @JsonProperty("predicted_score")
    private Double predictedScore;
}
