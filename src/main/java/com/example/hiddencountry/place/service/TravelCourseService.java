package com.example.hiddencountry.place.service;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hiddencountry.global.status.ErrorStatus;
import com.example.hiddencountry.place.domain.Place;
import com.example.hiddencountry.place.domain.TravelCourse;
import com.example.hiddencountry.place.model.PlaceThumbnailModel;
import com.example.hiddencountry.place.model.TravelCourseCreateRequest;
import com.example.hiddencountry.place.model.TravelCourseDetailModel;
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

	public TravelCourseDetailModel getTravelCourseDetail(Long courseId, User user) {
		TravelCourse course = travelCourseRepository.findById(courseId)
			.orElseThrow(ErrorStatus.PLACE_COURSE_NOT_FOUND::serviceException);

		// // 유저 좌표 가져오기 (옵션)
		// Double userLatitude = userLocationService.getLatitude(userId);
		// Double userLongitude = userLocationService.getLongitude(userId);

		List<PlaceThumbnailModel> places = course.getTravelCoursePlaces().stream()
			.map(tp -> {
				Place place = tp.getPlace();
				Boolean isBookmarked = commonPlaceService.isBookmarked(user, place);
				return PlaceThumbnailModel.toPlaceThumbnailModel(place, isBookmarked, null,null);
			})
			.toList();

		return new TravelCourseDetailModel(
			course.getId(),
			course.getName(),
			course.getFirstImage(),
			places
		);
	}

	@Transactional(readOnly = true)
	public List<TravelCourseModel> getMyTravelCourses(User user) {
		List<TravelCourse> courses = travelCourseRepository.findByUser(user);
		return courses.stream()
			.map(course -> new TravelCourseModel(
				course.getId(),
				course.getName(),
				course.getFirstImage()
			))
			.toList();
	}
}