package com.example.hiddencountry.review.model.response;

import com.example.hiddencountry.review.domain.Review;
import com.example.hiddencountry.review.domain.ReviewImage;

public record MyPageReviewResponse(
        Long id,
        Long placeId,
        String placeTitle,
        String content,
        Integer score,
        String placeImageUrl
) {
    public static MyPageReviewResponse from(Review review) {
        return new MyPageReviewResponse(
                review.getId(),
                review.getPlace().getId(),
                review.getPlace().getTitle(),
                review.getContent(),
                review.getScore(),
                review.getImages().stream().map(ReviewImage::getUrl).findFirst().orElse(null)
        );
    }
}
