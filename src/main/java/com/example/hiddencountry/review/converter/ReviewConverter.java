package com.example.hiddencountry.review.converter;

import com.example.hiddencountry.place.domain.Place;
import com.example.hiddencountry.review.domain.Review;
import com.example.hiddencountry.review.domain.ReviewTag;
import com.example.hiddencountry.review.domain.type.Tag;
import com.example.hiddencountry.review.model.request.ReviewRequest;
import com.example.hiddencountry.user.domain.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewConverter {
    public static Review reviewOf(User user, Place place, ReviewRequest req) {
        Review review = Review.builder()
                .content(req.content())
                .score(req.score())
                .place(place)
                .user(user)
                .build();

        if (req.tags() != null && !req.tags().isEmpty()) {
            for (Tag tag : req.tags()) {
                review.getTags().add(
                        ReviewTag.builder()
                                .tag(tag)
                                .review(review)
                                .build()
                );
            }
        }
        return review;
    }
}
