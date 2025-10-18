package com.example.hiddencountry.place.service;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hiddencountry.place.domain.Place;
import com.example.hiddencountry.place.domain.TravelCourse;
import com.example.hiddencountry.place.model.TravelCourseCreateRequest;
import com.example.hiddencountry.place.model.TravelCourseModel;
import com.example.hiddencountry.place.repository.PlaceRepository;
import com.example.hiddencountry.place.repository.TravelCourseRepository;
import com.example.hiddencountry.place.service.module.CommonPlaceService;
import com.example.hiddencountry.user.domain.User;

@Service
@RequiredArgsConstructor
@Transactional
public class TravelCourseService {

	private final TravelCourseRepository travelCourseRepository;
	private final CommonPlaceService commonPlaceService;

	public Long createTravelCourse(TravelCourseCreateRequest request, User user) {

		TravelCourse course = TravelCourse.builder()
			.name(request.getName())
			.user(user)
			.build();

		List<Long> placeIds = request.getPlaceIds();
		String firstImage = null;

		for (int i = 0; i < placeIds.size(); i++) {
			Long placeId = placeIds.get(i);
			Place place = commonPlaceService.findById(placeId);
			// 첫 번째로 유효한 이미지만 설정
			if (firstImage == null && place.getFirstImage() != null && !place.getFirstImage().isBlank()) {
				firstImage = place.getFirstImage();
			}
			course.addPlace(place, i + 1); // 순서 = 리스트 인덱스 + 1
		}

		// 코스에 첫 이미지 설정
		if (firstImage != null) {
			course.updateFirstImage(firstImage);
		}

		return travelCourseRepository.save(course).getId();
	}

	public List<TravelCourseModel> getAllTravelCourses() {
		List<TravelCourse> courses = travelCourseRepository.findAll();
		return courses.stream()
			.map(course -> new TravelCourseModel(
				course.getId(),
				course.getName(),
				course.getFirstImage()
			))
			.toList();
	}
}