package com.example.hiddencountry.place.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hiddencountry.global.annotation.HiddenCountryUser;
import com.example.hiddencountry.global.model.ApiResponse;
import com.example.hiddencountry.global.status.SuccessStatus;
import com.example.hiddencountry.place.model.TravelCourseCreateRequest;
import com.example.hiddencountry.place.service.TravelCourseService;
import com.example.hiddencountry.user.domain.User;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/travel-courses")
@RequiredArgsConstructor
public class TravelCourseController {

	private final TravelCourseService travelCourseService;

	@PostMapping
	public ApiResponse<Long> createTravelCourse(@RequestBody TravelCourseCreateRequest request,@Parameter(hidden = true) @HiddenCountryUser User user) {
		Long courseId = travelCourseService.createTravelCourse(request,user);
		return ApiResponse.onSuccess(
			SuccessStatus.PLACE_REQUEST_SUCCESS,
			courseId
		);
	}
}