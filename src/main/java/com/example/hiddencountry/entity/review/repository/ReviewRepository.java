package com.example.hiddencountry.entity.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hiddencountry.entity.review.domain.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
