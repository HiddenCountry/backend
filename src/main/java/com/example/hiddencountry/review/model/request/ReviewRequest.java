package com.example.hiddencountry.review.model.request;

import com.example.hiddencountry.review.domain.type.Tag;
import jakarta.validation.constraints.*;

import java.util.List;

public record ReviewRequest(
        @NotNull @Min(0) @Max(5) Integer score,
        @Size(max = 5, message = "태그는 최대 5개까지 등록 가능합니다.")
        List<Tag> tags,
        @NotBlank @Size(max = 1000) String content
) {
}
