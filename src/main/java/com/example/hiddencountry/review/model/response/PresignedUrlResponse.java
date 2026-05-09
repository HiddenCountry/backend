package com.example.hiddencountry.review.model.response;

public record PresignedUrlResponse(
        String presignedUrl,
        String s3Url
) {
    public static PresignedUrlResponse from(String presignedUrl, String s3Url) {
        return new PresignedUrlResponse(
                presignedUrl,
                s3Url
        );
    }
}
