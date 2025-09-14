package com.example.hiddencountry.review.model.response;

import java.util.List;

public record MyPageReviewListResponse(
        List<MyPageReviewResponse> myPageReviewResponses,
        long reviewCount,
        boolean hasNext,
        int page,
        int size
) {
}
