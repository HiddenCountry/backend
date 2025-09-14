package com.example.hiddencountry.review.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true, description = "정렬 옵션: LATEST | RATING_DESC")
public enum ReviewSort {
    LATEST,           // 최신순
    RATING_DESC       // 평점 높은순
}
