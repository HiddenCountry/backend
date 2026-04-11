package com.example.hiddencountry.review.model.response;

import com.example.hiddencountry.review.domain.Review;
import com.example.hiddencountry.review.domain.ReviewImage;
import com.example.hiddencountry.review.domain.ReviewTag;
import com.example.hiddencountry.review.domain.type.Tag;

import java.time.LocalDateTime;
import java.util.List;

public record ReviewResponse(
        Long id,
        Long placeId,
        Long userId,
        String userNickname,
        String content,
        Integer score,
        List<String> imageUrls,
        List<Tag> tags,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ReviewResponse from(Review review) {
        return from(
                review,
                review.getImages().stream().map(ReviewImage::getUrl).toList(),
                review.getTags().stream().map(ReviewTag::getTag).toList()
        );
    }

    public static ReviewResponse from(Review review, List<String> imageUrls, List<Tag> tags) {
        return new ReviewResponse(
                review.getId(),
                review.getPlace().getId(),
                review.getUser().getId(),
                review.getUser().getNickname(),
                review.getContent(),
                review.getScore(),
                imageUrls,
                tags,
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }
}
