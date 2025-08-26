package com.example.hiddencountry.place.controller;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.hiddencountry.global.annotation.HiddenCountryUser;
import com.example.hiddencountry.global.model.ApiResponse;
import com.example.hiddencountry.global.pagination.PaginationModel;
import com.example.hiddencountry.global.status.SuccessStatus;
import com.example.hiddencountry.place.domain.type.AreaCode;
import com.example.hiddencountry.place.domain.type.Cat1;
import com.example.hiddencountry.place.domain.type.ContentType;
import com.example.hiddencountry.place.domain.type.CountryRegion;
import com.example.hiddencountry.place.domain.type.Season;
import com.example.hiddencountry.place.domain.type.SortType;
import com.example.hiddencountry.place.model.PlaceThumbnailModel;
import com.example.hiddencountry.place.service.PlaceService;
import com.example.hiddencountry.user.domain.User;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PlaceController {

	private final PlaceService placeService;
	// 검색 아닐 때,
	// filter : Cat1 , ContentType, Season
	// sort : 리뷰 많은 순, 거리순, 조회순 , 평점 순

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/place")
	public ApiResponse<PaginationModel<PlaceThumbnailModel>> getPlace(
		@RequestParam @NotNull Integer page,
		@RequestParam @NotNull Integer size,
		@RequestParam(required = false) AreaCode areaCode,
		@RequestParam(required = false)  ContentType contentType,
		@RequestParam(required = false)  Season season,
		@RequestParam @NotNull CountryRegion countryRegion,
		@RequestParam @NotNull SortType sortType,
		@RequestParam(required = false) Double latitude,
		@RequestParam(required = false) Double longitude,
		@RequestParam(required = false) String title,
		@HiddenCountryUser User user
	) {
		return ApiResponse.onSuccess(
			SuccessStatus.OK,
			placeService.getPlaceThumbnailsWithSorting(
			user,page,size,areaCode,contentType,season,countryRegion,sortType,latitude,longitude,title
		));
	}

}
