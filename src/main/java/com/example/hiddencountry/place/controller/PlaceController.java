package com.example.hiddencountry.place.controller;
import java.util.List;

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
import com.example.hiddencountry.place.model.InfoItemModel;
import com.example.hiddencountry.place.model.PlaceDetailInfoModel;
import com.example.hiddencountry.place.model.PlaceThumbnailModel;
import com.example.hiddencountry.place.service.PlaceService;
import com.example.hiddencountry.place.service.KorApiService;
import com.example.hiddencountry.user.domain.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PlaceController {

	private final PlaceService placeService;
	private final KorApiService korApiService;

	// 검색 아닐 때,
	// filter : Cat1 , ContentType, Season
	// sort : 리뷰 많은 순, 거리순, 조회순 , 평점 순

	@Operation(
		summary = "장소 리스트 API",
		description = ""
	)
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/places")
	public ApiResponse<PaginationModel<PlaceThumbnailModel>> getPlace(
		@RequestParam @NotNull @Parameter(description = "페이지 번호 - 0 부터 시작", required = true, example = "0") Integer page,
		@RequestParam @NotNull @Parameter(description = "한 페이지 크기", required = true, example = "9") Integer size,
		@RequestParam(required = false) @Parameter(description = "지역", required = false, example = "SEOUL") AreaCode areaCode,
		@RequestParam(required = false) @Parameter(description = "관광 타입", required = false, example = "TOURIST_SPOT") ContentType contentType,
		@RequestParam(required = false) @Parameter(description = "계절", required = false, example = "ALL") Season season,
		@RequestParam @NotNull @Parameter(description = "나라", required = true, example = "NORTH_AMERICA") CountryRegion countryRegion,
		@RequestParam @NotNull @Parameter(description = "정렬 방법", required = true, example = "REVIEW_COUNT_DESC") SortType sortType,
		@RequestParam(required = false) @Parameter(description = "위도", required = false , example = "37.5547") Double latitude,
		@RequestParam(required = false) @Parameter(description = "경도", required = false , example = "126.9707") Double longitude,
		@RequestParam(required = false) @Parameter(description = "검색어 - 없어도 가능", required = false) String title,
		@Parameter(hidden = true) @HiddenCountryUser User user
	) {
		return ApiResponse.onSuccess(
			SuccessStatus.OK,
			placeService.getPlaceThumbnailsWithSorting(
			user,page,size,areaCode,contentType,season,countryRegion,sortType,latitude,longitude,title
		));
	}


	@Operation(
		summary = "장소 상세정보 API",
		description = ""
	)
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/place")
	public ApiResponse<PlaceDetailInfoModel> getPlace(
		//@RequestParam Long contentId,
		@RequestParam Long id,
		@RequestParam(required = false) @Parameter(description = "위도", required = false , example = "37.5547") Double latitude,
		@RequestParam(required = false) @Parameter(description = "경도", required = false , example = "126.9707") Double longitude,
		@Parameter(hidden = true) @HiddenCountryUser User user
	) {

		return ApiResponse.onSuccess(
			SuccessStatus.OK,
			placeService.dd(user,id,latitude,longitude)
		);
	}

	// 인근 관광지

	// 일반 관광지 상세정보


}
