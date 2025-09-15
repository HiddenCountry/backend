package com.example.hiddencountry.place.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.hiddencountry.global.pagination.PaginationModel;
import com.example.hiddencountry.place.domain.Location;
import com.example.hiddencountry.place.domain.Place;
import com.example.hiddencountry.place.domain.type.AreaCode;
import com.example.hiddencountry.place.domain.type.ContentType;
import com.example.hiddencountry.place.domain.type.CountryRegion;
import com.example.hiddencountry.place.domain.type.Season;
import com.example.hiddencountry.place.domain.type.SortType;
import com.example.hiddencountry.place.model.DetailCommon2Model;
import com.example.hiddencountry.place.model.InfoItemModel;
import com.example.hiddencountry.place.model.PlaceDetailInfoModel;
import com.example.hiddencountry.place.model.PlaceDistanceModel;
import com.example.hiddencountry.place.model.PlaceThumbnailModel;
import com.example.hiddencountry.place.repository.PlaceRepository;
import com.example.hiddencountry.place.service.module.CommonPlaceService;
import com.example.hiddencountry.user.domain.User;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceService {

	private final PlaceRepository placeRepository;
	private final CommonPlaceService commonPlaceService;
	private final KorApiService korApiService;

	public PaginationModel<PlaceThumbnailModel> getPlaceThumbnailsWithSorting(
		User user,
		Integer page,
		Integer size,
		List<AreaCode> areaCode,
		List<ContentType> contentType,
		List<Season> season,
		CountryRegion countryRegion,
		SortType sortType,
		Double userLatitude,
		Double userLongitude,
		String title
	) {
		log.info(
			"getThumbnailsByFilterAndSort called with page={}, size={}, areaCode={}, contentType={}, season={}, countryRegion={}, sortType={}, latitude={}, longitude={}, title='{}'",
			page, size, areaCode, contentType, season, countryRegion, sortType, userLatitude, userLongitude, title);
		if (sortType != SortType.DISTANCE_ASC) {
			Pageable pageable = PageRequest.of(page, size, Sort.by(sortType.getDirection(), sortType.getColumnName()));

			Page<Place> placePage = (title == null || !title.isBlank()) ?
				placeRepository.searchAndNotDistance(areaCode, title, contentType, season, countryRegion, pageable) :
				placeRepository.findFiltered(areaCode, contentType, season, countryRegion, pageable);
			List<PlaceThumbnailModel> thumbnails = placePage.getContent().stream()
				.map(place -> PlaceThumbnailModel.toPlaceThumbnailModel(
					place, commonPlaceService.isBookmarked(user, place), userLatitude,userLongitude
				))
				.toList();
			return PaginationModel.toPaginationModel(thumbnails, placePage);
		} else {
			Pageable pageable = PageRequest.of(page, size);
			Page<PlaceDistanceModel> placePage =
				(title == null || !title.isBlank()) ?
					placeRepository.searchAndDistance(userLatitude, userLongitude, areaCode, title, contentType, season,
						countryRegion, pageable) :
					placeRepository.findFilteredByDistance(userLatitude, userLongitude, areaCode, contentType, season,
						countryRegion, pageable);
			List<PlaceThumbnailModel> thumbnails = placePage.getContent().stream()
				.map(p -> PlaceThumbnailModel.toPlaceThumbnailModel(p.getPlace(),  // hashtags
					commonPlaceService.isBookmarked(user, p.getPlace()), userLatitude, userLongitude))
				.toList();
			return PaginationModel.toPaginationModel(thumbnails, placePage);
		}
	}

	public List<PlaceThumbnailModel> getPlaceThumbnailsForMap(User user, List<ContentType> contentType,
		List<CountryRegion> countryRegion, Double swLat, Double swLng, Double neLat, Double neLng,
		Double userLatitude,
		Double userLongitude) {
		List<Place> places = placeRepository.findByMapBoundsAndContentTypeAndCountry(contentType, countryRegion, swLat,
			swLng, neLat, neLng);

		return places.stream()
			.map(place -> PlaceThumbnailModel.toPlaceThumbnailModel(
				place, commonPlaceService.isBookmarked(user, place), userLatitude, userLongitude
			))
			.toList();

	}

	public PlaceDetailInfoModel getPlaceDetailInfo(
		User user,
		Long id,
		Long contentId,
		Integer contentType,
		Double latitude,
		Double longitude
	) {
		// 1. API 호출
		List<InfoItemModel> infoItemList = korApiService.getDetailIntro(contentId, contentType).block();
		DetailCommon2Model detail = korApiService.getDetailOverview2(contentId);
		Objects.requireNonNull(infoItemList).add(0, new InfoItemModel("개요", detail.getOverview()));

		// 2. 위치 정보 파싱
		Double lat = parseDoubleSafe(detail.getMapy());
		Double lon = parseDoubleSafe(detail.getMapx());

		// 4. DB 값은 선택적
		Float reviewScoreAverage = null;
		Boolean isBookmarked = null;
		Boolean isExoticPlace = id != null;
		List<String> countryNames;

		if (id != null) {
			Place place = commonPlaceService.findById(id);
			reviewScoreAverage = place.getReviewScoreAverage();
			isBookmarked = commonPlaceService.isBookmarked(user, place);
			countryNames = place.getPlaceCountries()
				.stream()
				.map(country -> country.getId().getCountryRegion().getDisplayName())
				.toList();
		} else {
			countryNames = Collections.emptyList();
		}

		// 5. 모델 생성
		return PlaceDetailInfoModel.of(
			id,
			detail.getTitle(),
			reviewScoreAverage,
			detail.getAddr1(),
			ContentType.fromCode(parseIntSafe(detail.getContenttypeid())).getName(),
			countryNames,
			infoItemList,
			lat,
			lon,
			latitude,
			longitude,
			isBookmarked,
			isExoticPlace
		);
	}

	private Double parseDoubleSafe(String s) {
		try {
			return s != null ? Double.parseDouble(s) : null;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private Integer parseIntSafe(String s) {
		try {
			return s != null ? Integer.parseInt(s) : null;
		} catch (NumberFormatException e) {
			return null;
		}
	}

}
