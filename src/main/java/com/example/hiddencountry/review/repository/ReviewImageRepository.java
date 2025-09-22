package com.example.hiddencountry.review.repository;

import com.example.hiddencountry.review.domain.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.zip.ZipFile;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    // 평점 있는 리뷰 + 이미지 있는 것 → score desc, id desc
    Optional<ReviewImage> findFirstByReview_Place_IdAndUrlIsNotNullAndReview_ScoreIsNotNullOrderByReview_ScoreDescReview_IdDesc(Long placeId);

    // 최신순(평점 무관) + 이미지 있는 것 → id desc
    Optional<ReviewImage> findFirstByReview_Place_IdAndUrlIsNotNullOrderByReview_IdDesc(Long placeId);

    List<ReviewImage> findTop30ByReview_Place_IdAndUrlIsNotNullAndReview_ScoreIsNotNullOrderByReview_ScoreDescReview_IdDescIdDesc(Long placeId);}
