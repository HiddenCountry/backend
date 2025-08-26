package com.example.hiddencountry.place.service.module;

import org.springframework.stereotype.Service;

import com.example.hiddencountry.global.status.ErrorStatus;
import com.example.hiddencountry.place.domain.Place;
import com.example.hiddencountry.place.repository.PlaceRepository;
import com.example.hiddencountry.place.repository.UserPlaceRepository;
import com.example.hiddencountry.user.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonPlaceService {

	private final UserPlaceRepository userPlaceRepository;
	private final PlaceRepository placeRepository;

	/**
	 * 즐겨찾기 여부 확인
	 */
	public boolean isBookmarked(User user, Place place) {
		// Place place = findById(placeId);
		if(user==null) return false;
		return userPlaceRepository.existsByUserAndPlace(user, place);
	}

	public Place findById(Long id){
		return placeRepository.findById(id).orElseThrow(ErrorStatus.PLACE_NOT_FOUND::serviceException);
	}

}
