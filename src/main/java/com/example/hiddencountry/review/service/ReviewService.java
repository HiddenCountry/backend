package com.example.hiddencountry.review.service;

import com.example.hiddencountry.global.status.ErrorStatus;
import com.example.hiddencountry.global.storage.PresignedUrlResult;
import com.example.hiddencountry.global.storage.S3Uploader;
import com.example.hiddencountry.place.domain.Place;
import com.example.hiddencountry.place.domain.UserPlace;
import com.example.hiddencountry.place.model.PlaceThumbnailModel;
import com.example.hiddencountry.place.repository.PlaceRepository;
import com.example.hiddencountry.review.converter.ReviewConverter;
import com.example.hiddencountry.review.domain.Review;
import com.example.hiddencountry.review.domain.ReviewImage;
import com.example.hiddencountry.review.domain.type.Tag;
import com.example.hiddencountry.review.model.ReviewSort;
import com.example.hiddencountry.review.model.request.ReviewRequest;
import com.example.hiddencountry.review.model.response.MyPageReviewListResponse;
import com.example.hiddencountry.review.model.response.MyPageReviewResponse;
import com.example.hiddencountry.review.model.response.PresignedUrlResponse;
import com.example.hiddencountry.review.model.response.ReviewListResponse;
import com.example.hiddencountry.review.model.response.ReviewResponse;
import com.example.hiddencountry.review.repository.ReviewImageRepository;
import com.example.hiddencountry.review.repository.ReviewTagRepository;
import com.example.hiddencountry.user.domain.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.example.hiddencountry.review.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final ReviewTagRepository reviewTagRepository;
	private final ReviewImageRepository reviewImageRepository;
	private final PlaceRepository placeRepository;
	private final S3Uploader s3Uploader;

	/**
	 *
	 * @param placeId	리뷰 대상 장소 ID
	 * @param count	업로드 할 이미지 개수
	 * @return	PresignedUrl 및 s3Url(이미지 url)
	 */
	public List<PresignedUrlResponse> generatePresignedUrls(Long placeId, int count) {
		placeRepository.findById(placeId)
				.orElseThrow(ErrorStatus.PLACE_NOT_FOUND::serviceException);

		String dir = "reviews/" + placeId;
		return IntStream.range(0, count)
				.mapToObj(i -> s3Uploader.generatePresignedUrl(dir))
				.map(r -> PresignedUrlResponse.from(r.presignedUrl(), r.s3Url()))
				.toList();
	}

	/**
	 * 리뷰를 생성합니다
	 * @param user	리뷰 작성자(인증 사용자)
	 * @param placeId	리뷰 대상 장소 ID
	 * @param request	리뷰 본문/점수/태그 등 입력 DTO
	 * @return	생성된 리뷰의 응답 DTO
	 */
	@Transactional
	public ReviewResponse createReview(User user, Long placeId, @Valid ReviewRequest request) {
		Place place = placeRepository.findByIdForUpdate(placeId)
				.orElseThrow(ErrorStatus.PLACE_NOT_FOUND::serviceException);

		Review review = ReviewConverter.reviewOf(user, place, request);

		if (request.imageUrls() != null) {
			for (String url : request.imageUrls()) {
				review.getImages().add(ReviewImage.builder().url(url).review(review).build());
			}
		}

		reviewRepository.save(review);
		updateReviewStats(placeId);
		updateFirstImageIfNeeded(placeId);

		return ReviewResponse.from(review);
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
    @Transactional(readOnly = true)
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
			if (cursorScore == null || cursorId == null) {
				slice = reviewRepository.findByPlace_IdOrderByScoreDescIdDesc(placeId, p);
			} else {
				// OR 조건을 두 branch로 분리해 각각 인덱스를 완전히 활용
				// branch2: score = cursor → id DESC (높은 score 우선이므로 결과 앞부분)
				// branch1: score < cursor → score DESC, id DESC (낮은 score는 결과 뒷부분)
				Pageable limit = PageRequest.of(0, size + 1);
				List<Review> branch2 = reviewRepository.findByScoreEqualAndIdLessThan(placeId, cursorScore, cursorId, limit);
				List<Review> branch1 = reviewRepository.findByScoreLessThan(placeId, cursorScore, limit);

				List<Review> merged = new ArrayList<>(branch2);
				merged.addAll(branch1);

				boolean hasNext = merged.size() > size;
				List<Review> content = merged.subList(0, Math.min(size, merged.size()));
				slice = new SliceImpl<>(content, p, hasNext);
			}
		}

		List<Review> reviews = slice.getContent();
		Map<Long, List<String>> imageUrlsByReviewId = getImageUrlsByReviewId(reviews);
		Map<Long, List<Tag>> tagsByReviewId = getTagsByReviewId(reviews);

		var results = reviews.stream()
				.map(review -> ReviewResponse.from(
						review,
						imageUrlsByReviewId.getOrDefault(review.getId(), List.of()),
						tagsByReviewId.getOrDefault(review.getId(), List.of())
				))
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

	private Map<Long, List<String>> getImageUrlsByReviewId(List<Review> reviews) {
		if (reviews.isEmpty()) {
			return Map.of();
		}

		Map<Long, List<String>> imageUrlsByReviewId = new LinkedHashMap<>();
		List<Long> reviewIds = reviews.stream().map(Review::getId).toList();
		reviewImageRepository.findImageViewsByReviewIdIn(reviewIds)
				.forEach(image -> imageUrlsByReviewId
						.computeIfAbsent(image.getReviewId(), ignored -> new ArrayList<>())
						.add(image.getUrl()));
		return imageUrlsByReviewId;
	}

	private Map<Long, List<Tag>> getTagsByReviewId(List<Review> reviews) {
		if (reviews.isEmpty()) {
			return Map.of();
		}

		Map<Long, List<Tag>> tagsByReviewId = new LinkedHashMap<>();
		List<Long> reviewIds = reviews.stream().map(Review::getId).toList();
		reviewTagRepository.findTagViewsByReviewIdIn(reviewIds)
				.forEach(tag -> tagsByReviewId
						.computeIfAbsent(tag.getReviewId(), ignored -> new ArrayList<>())
						.add(tag.getTag()));
		return tagsByReviewId;
	}

	/**
	 * 해당 장소의 리뷰 개수, 평점 평균, top 해시태그(2개)를 Place 엔티티에 반영합니다
	 * @param placeId 장소 Id
	 */
	@Transactional
	public void updateReviewStats(Long placeId) {
		long count = reviewRepository.countByPlace_Id(placeId);
		Double avg   = reviewRepository.avgScoreByPlaceId(placeId);
		float average = avg != null ? avg.floatValue() : 0f;

		List<Tag> topTags = reviewTagRepository.findTopTagValuesByPlace(placeId, PageRequest.of(0, 2));
		Tag top1 = topTags.size() > 0 ? topTags.get(0) : null;
		Tag top2 = topTags.size() > 1 ? topTags.get(1) : null;

		Place place = placeRepository.findById(placeId).orElseThrow();
		place.changeReviewStats(count, average);
		place.changeTopHashtags(top1, top2);
	}

	/**
	 * 리뷰 이미지로 장소의 firstImage 필드를 대체합니다.
	 * @param placeId 장소 Id
	 */
	@Transactional
	public void updateFirstImageIfNeeded(Long placeId) {
		Place place = placeRepository.findById(placeId)
				.orElseThrow(ErrorStatus.PLACE_NOT_FOUND::serviceException);

		// 공식 이미지가 있으면 유지
		if (StringUtils.hasText(place.getFirstImage()) && !place.isReviewImage()) {
			return;
		}

		String url = pickImageFromReviews(placeId);

		if (StringUtils.hasText(url) && !url.equals(place.getFirstImage())) {
			place.updateFirstImageFromReview(url);
		}

	}

	/**
	 * 리뷰의 (평점순 -> 최신순) 이미지 url을 반환합니다.
	 * @param placeId 장소 Id
	 * @return 리뷰 이미지 url
	 */
	private String pickImageFromReviews(Long placeId) {
		return reviewImageRepository
				.findFirstByReview_Place_IdAndUrlIsNotNullAndReview_ScoreIsNotNullOrderByReview_ScoreDescReview_IdDesc(placeId)
				.map(ReviewImage::getUrl)
				.or(() -> reviewImageRepository
						.findFirstByReview_Place_IdAndUrlIsNotNullOrderByReview_IdDesc(placeId)
						.map(ReviewImage::getUrl))
				.orElse(null);
	}

	/**
	 * 마이페이지에서 로그인한 사용자가 작성한 리뷰를 조회합니다
	 * @param user 리뷰 작성자(로그인한 사용자)
	 * @param page 페이지 번호
	 * @param size 한 페이지 크기
	 * @return 페이징된 리뷰 목록, 작성한 전체 리뷰 개수, 다음 페이지 존재 여부, 페이지 인덱스, 페이지 크기
	 */
	@Transactional(readOnly = true)
	public MyPageReviewListResponse getUserReviews(User user, @NotNull Integer page, @NotNull Integer size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

		Page<Review> resultPage = reviewRepository.findByUser_IdOrderByIdDesc(user.getId(), pageable);

		List<MyPageReviewResponse> items = resultPage.getContent().stream()
				.map(MyPageReviewResponse::from)
				.toList();

		long total = resultPage.getTotalElements();

		return new MyPageReviewListResponse(items, total, resultPage.hasNext(), page, size);
	}

	/**
	 * 리뷰의 이미지 url들을 반환합니다. (최대 30개)
	 * @param placeId
	 * @return 장소 Id
	 */
	@Transactional(readOnly = true)
	public List<String> getReviewImages(Long placeId) {
		placeRepository.findById(placeId)
				.orElseThrow(ErrorStatus.PLACE_NOT_FOUND::serviceException);

		// 최대 30개
		return reviewImageRepository
				.findTop30ByReview_Place_IdAndUrlIsNotNullAndReview_ScoreIsNotNullOrderByReview_ScoreDescReview_IdDescIdDesc(placeId)
				.stream()
				.map(ReviewImage::getUrl)
				.filter(url -> url != null && !url.isBlank()) // 빈 문자열 방지
				.toList();
	}
}
