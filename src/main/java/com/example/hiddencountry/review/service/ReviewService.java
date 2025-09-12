package com.example.hiddencountry.review.service;

import com.example.hiddencountry.global.status.ErrorStatus;
import com.example.hiddencountry.global.storage.S3Uploader;
import com.example.hiddencountry.place.domain.Place;
import com.example.hiddencountry.place.domain.UserPlace;
import com.example.hiddencountry.place.repository.PlaceRepository;
import com.example.hiddencountry.review.converter.ReviewConverter;
import com.example.hiddencountry.review.domain.Review;
import com.example.hiddencountry.review.domain.ReviewImage;
import com.example.hiddencountry.review.domain.ReviewTag;
import com.example.hiddencountry.review.domain.type.Tag;
import com.example.hiddencountry.review.model.ReviewSort;
import com.example.hiddencountry.review.model.request.ReviewRequest;
import com.example.hiddencountry.review.model.response.ReviewListResponse;
import com.example.hiddencountry.review.model.response.ReviewResponse;
import com.example.hiddencountry.user.domain.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.hiddencountry.review.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final PlaceRepository placeRepository;
	private final S3Uploader s3Uploader;

	/**
	 * 리뷰를 생성합니다
	 * @param user    리뷰 작성자(인증 사용자)
	 * @param placeId 리뷰 대상 장소 ID
	 * @param request 리뷰 본문/점수/태그 등 입력 DTO
	 * @param images  업로드할 이미지 파일 목록. 비어있으면 업로드를 생략합니다.
	 * @return 생성된 리뷰의 응답 DTO
	 * @throws IOException 이미지 업로드(S3) 과정에서 I/O 오류가 발생한 경우
	 */
	@Transactional
	public ReviewResponse createReview(User user, Long placeId, @Valid ReviewRequest request, List<MultipartFile> images) throws IOException {

		Place place = placeRepository.findById(placeId)
				.orElseThrow(ErrorStatus.PLACE_NOT_FOUND::serviceException);

		Review review = ReviewConverter.reviewOf(user, place, request);

		Review saved = reviewRepository.save(review);

		if (images != null && !images.isEmpty()) {
			String dir = "reviews/%d/%d".formatted(placeId, saved.getId());
			for (MultipartFile file : images) {
				String url = s3Uploader.upload(file, dir);
				saved.getImages().add(
						ReviewImage.builder().url(url).review(saved).build()
				);
			}
		}

		return ReviewResponse.from(saved);
	}

	/**
	 * 리뷰 목록을 cursor 기반으로 조회합니다
	 * @param placeId 리뷰 대상 장소 ID
	 * @param sort 정렬 기준 (LATEST 최신순, RATING_DESC 평점 높은 순)
	 * @param cursorId 이전 페이지의 마지막 리뷰 ID를 전달
	 * @param cursorScore 이전 페이지의 마지막 리뷰 점수를 전달
	 * @param size 페이지 크기
	 * @return 리뷰 목록 DTO
	 */
    public ReviewListResponse getReviews(Long placeId, ReviewSort sort,
										 Long cursorId, Integer cursorScore, int size) {
		Slice<Review> slice;

		if (sort == ReviewSort.LATEST) {
			Pageable p = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));
			slice = (cursorId == null)
					? reviewRepository.findByPlace_IdOrderByIdDesc(placeId, p)
					: reviewRepository.findByPlace_IdAndIdLessThanOrderByIdDesc(placeId, cursorId, p);

		} else { // RATING_DESC
			Pageable p = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "score", "id"));
			slice = (cursorScore == null || cursorId == null)
					? reviewRepository.findByPlace_IdOrderByScoreDescIdDesc(placeId, p)
					: reviewRepository.findTopRatedAfter(placeId, cursorScore, cursorId, p);
		}

		var results = slice.getContent().stream()
				.map(ReviewResponse::from)
				.toList();

		Long nextId = null;
		Integer nextScore = null;
		if (slice.hasNext() && !slice.isEmpty()) {
			Review last = slice.getContent().get(results.size() - 1);
			nextId = last.getId();
			if (sort == ReviewSort.RATING_DESC) nextScore = last.getScore();
		}

		return ReviewListResponse.builder()
				.reviewResponses(results)
				.hasNext(slice.hasNext())
				.nextId(nextId)
				.nextScore(nextScore)
				.build();
    }
}
