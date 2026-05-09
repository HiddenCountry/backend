package com.example.hiddencountry.review.controller;

import com.example.hiddencountry.global.annotation.HiddenCountryUser;
import com.example.hiddencountry.global.model.ApiResponse;
import com.example.hiddencountry.global.status.SuccessStatus;
import com.example.hiddencountry.review.model.ReviewSort;
import com.example.hiddencountry.review.model.request.PresignedUrlRequest;
import com.example.hiddencountry.review.model.request.ReviewRequest;
import com.example.hiddencountry.review.model.response.MyPageReviewListResponse;
import com.example.hiddencountry.review.model.response.PresignedUrlResponse;
import com.example.hiddencountry.review.model.response.ReviewListResponse;
import com.example.hiddencountry.review.model.response.ReviewResponse;
import com.example.hiddencountry.review.service.ReviewService;
import com.example.hiddencountry.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 이미지 Presigned URL 발급 API",
               description = "클라이언트가 S3에 직접 업로드할 Presigned URL을 발급합니다. " +
                             "발급된 presignedUrl로 PUT 요청해 이미지를 업로드한 뒤, " +
                             "s3Url을 리뷰 등록 API의 imageUrls에 담아 전송하세요. " +
                             "Presigned URL 유효 시간은 10분입니다.")
    @PostMapping("/{placeId}/presigned-urls")
    public ApiResponse<List<PresignedUrlResponse>> getPresignedUrls(
            @Parameter(hidden = true) @HiddenCountryUser User user,
            @PathVariable("placeId") Long placeId,
            @Valid @RequestBody PresignedUrlRequest request
    ) {
        return ApiResponse.onSuccess(
                SuccessStatus.REVIEW_PRESIGNED_URL_SUCCESS,
                reviewService.generatePresignedUrls(placeId, request.count()));
    }

    @Operation(summary = "리뷰 등록 API",
               description = "이미지가 있는 경우 먼저 presigned-urls API로 URL을 발급받아 S3에 업로드한 뒤, " +
                             "반환된 s3Url 목록을 imageUrls에 담아 요청하세요.")
    @PostMapping("/{placeId}")
    public ApiResponse<ReviewResponse> create(
            @Parameter(hidden = true) @HiddenCountryUser User user,
            @PathVariable("placeId") Long placeId,
            @Valid @RequestBody ReviewRequest request
    ) {
        return ApiResponse.onSuccess(
                SuccessStatus.REVIEW_CREATE_SUCCESS,
                reviewService.createReview(user, placeId, request));
    }

    @Operation(summary = "리뷰 목록 조회 API",
               description = """
  커서(키셋) 기반 무한스크롤 조회입니다.
  - `LATEST`  : id DESC (최신순)
  - `RATING_DESC`: score DESC, id DESC (평점 높은 순)

  - LATEST      : 다음 페이지 요청 시 `cursorId` = 이전 응답의 `nextId`
  - RATING_DESC : 다음 페이지 요청 시 `cursorScore` = 이전 응답의 `nextScore`, `cursorId` = 이전 응답의 `nextId`
""")
    @GetMapping("/{placeId}")
    public ApiResponse<ReviewListResponse> list(
            @PathVariable("placeId") Long placeId,
            @RequestParam(defaultValue = "LATEST") ReviewSort sort,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false) Integer cursorScore,
            @RequestParam(defaultValue = "10") int size,
            @Parameter(hidden = true) @HiddenCountryUser User user
    ) {
        return ApiResponse.onSuccess(
                SuccessStatus.OK,
                reviewService.getReviews(placeId, sort, cursorId, cursorScore, size)
        );
    }

    @Operation(
            summary = "리뷰 이미지 조회 (최대 30개)",
            description = "특정 장소의 리뷰 이미지들을 반환합니다."
    )
    @GetMapping("/{placeId}/images")
    public ApiResponse<List<String>> getReviewImages(
            @PathVariable("placeId") Long placeId
    ) {
        return ApiResponse.onSuccess(
                SuccessStatus.OK,
                reviewService.getReviewImages(placeId)
        );
    }

    @Operation(
            summary = "마이페이지 내가 작성한 리뷰 조회",
            description = ""
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/mypage")
    public ApiResponse<MyPageReviewListResponse> getUserReviews(
            @Parameter(hidden = true) @HiddenCountryUser User user,
            @RequestParam(defaultValue = "0") @NotNull @Parameter(description = "페이지 번호 - 0 부터 시작", required = true, example = "0") Integer page,
            @RequestParam(defaultValue = "5") @NotNull @Parameter(description = "한 페이지 크기", required = true, example = "5") Integer size
    ) {
        return ApiResponse.onSuccess(
                SuccessStatus.OK,
                reviewService.getUserReviews(user, page, size)
        );
    }
}
