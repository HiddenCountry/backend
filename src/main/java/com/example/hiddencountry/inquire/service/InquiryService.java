package com.example.hiddencountry.inquire.service;

import org.springframework.stereotype.Service;

import com.example.hiddencountry.inquire.domain.Inquiry;
import com.example.hiddencountry.inquire.model.InquiryRequest;
import com.example.hiddencountry.inquire.repository.InquiryRepository;
import com.example.hiddencountry.user.domain.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InquiryService {

	private final InquiryRepository inquiryRepository;

	@Transactional
	public Inquiry saveInquiry(InquiryRequest request, User user) {

		Inquiry inquiry = Inquiry.builder()
			.title(request.getTitle())
			.content(request.getContent())
			.user(user)
			.build();

		return inquiryRepository.save(inquiry);
	}
}
