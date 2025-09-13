package com.example.hiddencountry.review.model.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ReviewListResponse(
        List<ReviewResponse> reviewResponses,
        boolean hasNext,
        Long nextId,
        Integer nextScore
) {

}
