package com.example.hiddencountry.review.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PresignedUrlRequest(
        @NotNull @Min(1) @Max(5) Integer count
) {}
