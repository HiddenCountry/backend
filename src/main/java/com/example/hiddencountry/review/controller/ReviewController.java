package com.example.hiddencountry.review.controller;

import com.example.hiddencountry.global.annotation.HiddenCountryUser;
import com.example.hiddencountry.global.model.ApiResponse;
import com.example.hiddencountry.global.status.SuccessStatus;
import com.example.hiddencountry.review.model.request.ReviewRequest;
import com.example.hiddencountry.review.model.response.ReviewResponse;
import com.example.hiddencountry.review.service.ReviewService;
import com.example.hiddencountry.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/review/{placeId}")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 등록 API")
    @RequestBody(
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(type = "object", requiredProperties = {"request"}),
                    schemaProperties = {
                            @SchemaProperty(name = "request",
                                    schema = @Schema(implementation = ReviewRequest.class) // JSON DTO
                            ),
                            @SchemaProperty(name = "images",
                                    array = @ArraySchema(schema = @Schema(type = "string", format = "binary")) // 파일 배열
                            )
                    },
                    encoding = {
                            @Encoding(name = "request", contentType = MediaType.APPLICATION_JSON_VALUE),
                            @Encoding(name = "images",  contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
                    }
            )
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ReviewResponse> create(
            @Parameter(hidden = true) @HiddenCountryUser User user,
            @PathVariable("placeId") Long placeId,
            @Valid @RequestPart("request") ReviewRequest request, // JSON 파트
            @RequestPart(name = "images", required = false) List<MultipartFile> images
    ) throws IOException {
        return ApiResponse.onSuccess(
                SuccessStatus.REVIEW_CREATE_SUCCESS,
                reviewService.createReview(user, placeId, request, images));
    }
}
