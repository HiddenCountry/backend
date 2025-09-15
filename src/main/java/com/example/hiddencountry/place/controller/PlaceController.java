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
import com.example.hiddencountry.place.domain.type.ContentType;
import com.example.hiddencountry.place.domain.type.CountryRegion;
import com.example.hiddencountry.place.domain.type.Season;
import com.example.hiddencountry.place.domain.type.SortType;
import com.example.hiddencountry.place.model.PlaceDetailInfoModel;
import com.example.hiddencountry.place.model.PlaceThumbnailModel;
import com.example.hiddencountry.place.service.PlaceService;
import com.example.hiddencountry.user.domain.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PlaceController {

	private final PlaceService placeService;

	@Operation(
		summary = "장소 리스트 API",
		description = ""
	)
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/places")
	public ApiResponse<PaginationModel<PlaceThumbnailModel>> getPlace(
		@RequestParam @NotNull @Parameter(description = "페이지 번호 - 0 부터 시작", required = true, example = "0") Integer page,
		@RequestParam @NotNull @Parameter(description = "한 페이지 크기", required = true, example = "9") Integer size,
		@RequestParam(required = false) @Parameter(description = "지역(복수선택)", required = false, example = "SEOUL") List<AreaCode> areaCode,
		@RequestParam(required = false) @Parameter(description = "관광 타입(복수선택)", required = false, example = "TOURIST_SPOT") List<ContentType> contentType,
		@RequestParam(required = false) @Parameter(description = "계절(복수선택)", required = false, example = "ALL") List<Season> season,
		@RequestParam @NotNull @Parameter(description = "나라", required = true, example = "NORTH_AMERICA") CountryRegion countryRegion,
		@RequestParam @NotNull @Parameter(description = "정렬 방법", required = true, example = "REVIEW_COUNT_DESC") SortType sortType,
		@RequestParam(required = false) @Parameter(description = "사용자 위도", required = false , example = "37.5547") Double userLat,
		@RequestParam(required = false) @Parameter(description = "사용자 경도", required = false , example = "126.9707") Double userLng,
		@RequestParam(required = false) @Parameter(description = "검색어 - 없어도 가능", required = false) String title,
		@Parameter(hidden = true) @HiddenCountryUser User user
	) {
		return ApiResponse.onSuccess(
			SuccessStatus.OK,
			placeService.getPlaceThumbnailsWithSorting(
			user,page,size,areaCode,contentType,season,countryRegion,sortType,userLat,userLng,title
		));
	}

	@Operation(
		summary = "지도 API",
		description = ""
	)
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/places/map")
	public ApiResponse<List<PlaceThumbnailModel>> getPlace(
		@RequestParam(required = false) @Parameter(description = "관광 타입(복수 선택)", required = false, example = "TOURIST_SPOT") List<ContentType> contentTypes,
		@RequestParam(required = false)  @Parameter(description = "나라(복수 선택)", required = false, example = "NORTH_AMERICA") List<CountryRegion> countryRegions,
		@RequestParam(required = true) @Parameter(description = "남서쪽 위도", required = true, example = "37.5500") Double swLat,
		@RequestParam(required = true) @Parameter(description = "남서쪽 경도", required = true, example = "126.9600") Double swLng,
		@RequestParam(required = true) @Parameter(description = "북동쪽 위도", required = true, example = "37.5600") Double neLat,
		@RequestParam(required = true) @Parameter(description = "북동쪽 경도", required = true, example = "126.9800") Double neLng,
		@RequestParam(required = false) @Parameter(description = "사용자 위도", required = false , example = "37.5547") Double userLat,
		@RequestParam(required = false) @Parameter(description = "사용자 경도", required = false , example = "126.9707") Double userLng,
		@Parameter(hidden = true) @HiddenCountryUser User user
	) {
		return ApiResponse.onSuccess(
			SuccessStatus.OK,
			placeService.getPlaceThumbnailsForMap(
				user,contentTypes,countryRegions,swLat,swLng,neLat,neLng,userLat,userLng
			)
		);
	}


	@Operation(
		summary = "이색 관광지, 근처 관광지 상세정보 API",
		description = "id == null -> 근처 관광지 상세페이지 <br> id != null -> 이국적 관광지 상세페이지"
	)
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/place")
	public ApiResponse<PlaceDetailInfoModel> getPlace(
		@RequestParam(required = true) @Parameter(description = "contentId", required = false , example = "900729") Long contentId,
		@RequestParam(required = true) @Parameter(description = "contentTypeId", required = false , example = "32") Integer contentTypeId,
		@RequestParam(required = false) @Parameter(description = "인근 관광지일 경우 null", required = false , example = "8") Long id,
		@RequestParam(required = false) @Parameter(description = "사용자 위도", required = false , example = "37.5547") Double userLat,
		@RequestParam(required = false) @Parameter(description = "사용자 경도", required = false , example = "126.9707") Double userLng,
		@Parameter(hidden = true) @HiddenCountryUser User user
	) {
		return ApiResponse.onSuccess(
			SuccessStatus.OK,
			placeService.getPlaceDetailInfo(user,id,contentId,contentTypeId,userLat,userLng)
		);
	}



}
