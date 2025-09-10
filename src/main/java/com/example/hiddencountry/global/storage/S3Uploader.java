package com.example.hiddencountry.global.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String upload(MultipartFile file, String dirName) throws IOException {
        String key = dirName + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
        ObjectMetadata md = new ObjectMetadata();
        md.setContentLength(file.getSize());
        if (file.getContentType() != null && !file.getContentType().isBlank()) md.setContentType(file.getContentType());

        try (InputStream in = file.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucketName, key, in, md));
        }
        return amazonS3.getUrl(bucketName, key).toString();
    }

    public void delete(String imageUrl) {
        String key = extractKeyFromUrl(imageUrl);
        amazonS3.deleteObject(bucketName, key);
    }

    private String extractKeyFromUrl(String url) {
        // https://bucket.s3.region.amazonaws.com/dir/a/b/file.jpg?x=y  →  dir/a/b/file.jpg
        String path = URI.create(url).getPath();
        return (path.startsWith("/") ? path.substring(1) : path);
    }

}
