package com.example.hiddencountry.place.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import com.example.hiddencountry.global.pagination.PaginationModel;
import com.example.hiddencountry.place.domain.Place;
import com.example.hiddencountry.place.domain.UserPlace;
import com.example.hiddencountry.place.model.PlaceThumbnailModel;
import com.example.hiddencountry.place.repository.UserPlaceRepository;
import com.example.hiddencountry.place.service.module.CommonPlaceService;
import com.example.hiddencountry.user.domain.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserPlaceService {

	private final UserPlaceRepository userPlaceRepository;
	private final CommonPlaceService commonPlaceService;

	/**
	 * 즐겨찾기 등록
	 */
	@Transactional
	public void addBookmarkPlace(User user, Long placeId) {

		Place place = commonPlaceService.findById(placeId);

		// 이미 즐겨찾기에 추가된 경우 무시
		if (userPlaceRepository.existsByUserAndPlace(user, place)) {
			log.info("User {} already bookmarked place {}", user.getId(), placeId);
			return;
		}

		UserPlace userPlace = UserPlace.of(user, place);
		userPlaceRepository.save(userPlace);
		log.info("User {} bookmarked place {}", user.getId(), placeId);
	}

	/**
	 * 즐겨찾기 삭제
	 */
	@Transactional
	public void removeBookmarkPlace(User user, Long placeId) {
		Place place = commonPlaceService.findById(placeId);

		userPlaceRepository.findByUserAndPlace(user, place)
			.ifPresent(up -> {
				userPlaceRepository.delete(up);
				log.info("User {} removed bookmark for place {}", user.getId(), placeId);
			});
	}

	public PaginationModel<PlaceThumbnailModel> getUserBookmarkPlaces(User user,Integer page, Integer size) {
		log.info(" {} ", user.getId() );

		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
		Page<UserPlace> userPlacePage = userPlaceRepository.findByUser(user, pageable);
		List<PlaceThumbnailModel> placeThumbnailModelList = userPlacePage.getContent().stream()
			.map(userPlace -> PlaceThumbnailModel.toPlaceThumbnailModel(
				userPlace.getPlace(),  true
			))
			.toList();
		return PaginationModel.toPaginationModel(placeThumbnailModelList,userPlacePage);
	}


}
