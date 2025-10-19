package com.example.hiddencountry.place.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hiddencountry.global.annotation.HiddenCountryUser;
import com.example.hiddencountry.global.model.ApiResponse;
import com.example.hiddencountry.global.status.SuccessStatus;
import com.example.hiddencountry.place.model.TravelCourseCreateRequest;
import com.example.hiddencountry.place.model.TravelCourseDetailModel;
import com.example.hiddencountry.place.model.TravelCourseModel;
import com.example.hiddencountry.place.service.TravelCourseService;
import com.example.hiddencountry.user.domain.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/travel-courses")
@RequiredArgsConstructor
public class TravelCourseController {

	private final TravelCourseService travelCourseService;

	@Operation(summary = "여행 코스 등록", description = "새로운 여행 코스를 등록합니다.")
	@PostMapping
	public ApiResponse<Long> createTravelCourse(@RequestBody TravelCourseCreateRequest request,@Parameter(hidden = true) @HiddenCountryUser User user) {
		Long courseId = travelCourseService.createTravelCourse(request,user);
		return ApiResponse.onSuccess(
			SuccessStatus.TRAVEL_COURSE_POST_SUCCESS,
			courseId
		);
	}

	@Operation(summary = "여행 코스 리스트", description = "")
	@GetMapping
	public ApiResponse<List<TravelCourseModel>> getTravelCourses() {
		List<TravelCourseModel> courses = travelCourseService.getAllTravelCourses();
		return ApiResponse.onSuccess(
			SuccessStatus.OK,
			courses
		);
	}

	@Operation(summary = "여행 코스 상세", description = "")
	@GetMapping("/{id}")
	public ApiResponse<TravelCourseDetailModel> getTravelCourseDetail(
		@PathVariable Long id,
		@Parameter(hidden = true) @HiddenCountryUser User user
	) {
		TravelCourseDetailModel response = travelCourseService.getTravelCourseDetail(id, user);
		return ApiResponse.onSuccess(
			SuccessStatus.OK,
			response
		);
	}

	@Operation(summary = "마이페이지 - 내가 등록한 코스 리스트", description = "")
	@GetMapping("/mine")
	public ApiResponse<List<TravelCourseModel>> getMyTravelCourses(
		@Parameter(hidden = true) @HiddenCountryUser User user
	) {
		List<TravelCourseModel> myCourses = travelCourseService.getMyTravelCourses(user);
		return ApiResponse.onSuccess(
			SuccessStatus.OK,
			myCourses
		);
	}

	@Operation(summary = "여행 코스 삭제", description = "")
	@DeleteMapping("/{id}")
	public ApiResponse<Void> deleteTravelCourse(
		@PathVariable Long id,
		@Parameter(hidden = true) @HiddenCountryUser User user
	) {
		travelCourseService.deleteTravelCourse(id, user);
		return ApiResponse.onSuccess(SuccessStatus.TRAVEL_COURSE_DELETE_SUCCESS, null);
	}
}