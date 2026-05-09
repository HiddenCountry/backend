package com.example.hiddencountry.global.storage;

public record PresignedUrlResult(
        String presignedUrl,
        String s3Url
) {
    public static PresignedUrlResult from(String presignedUrl, String s3Url) {
        return new PresignedUrlResult(
                presignedUrl,
                s3Url
        );
    }
}
