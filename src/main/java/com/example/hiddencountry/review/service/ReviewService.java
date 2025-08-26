package com.example.hiddencountry.review.service;

import org.springframework.stereotype.Service;

import com.example.hiddencountry.review.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;



}
