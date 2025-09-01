package com.example.hiddencountry.place.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.hiddencountry.global.pagination.PaginationModel;
import com.example.hiddencountry.place.domain.Place;
import com.example.hiddencountry.place.domain.type.AreaCode;
import com.example.hiddencountry.place.domain.type.ContentType;
import com.example.hiddencountry.place.domain.type.CountryRegion;
import com.example.hiddencountry.place.domain.type.Season;
import com.example.hiddencountry.place.domain.type.SortType;
import com.example.hiddencountry.place.model.InfoItemModel;
import com.example.hiddencountry.place.model.PlaceDetailInfoModel;
import com.example.hiddencountry.place.model.PlaceDistanceModel;
import com.example.hiddencountry.place.model.PlaceThumbnailModel;
import com.example.hiddencountry.place.repository.PlaceRepository;
import com.example.hiddencountry.place.service.module.CommonPlaceService;
import com.example.hiddencountry.user.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceService {

	private final PlaceRepository placeRepository;
	private final CommonPlaceService commonPlaceService;
	private final KorApiService korApiService;

	///  todo : 해시태그
	public PaginationModel<PlaceThumbnailModel> getPlaceThumbnailsWithSorting(
		User user,
		Integer page,
		Integer size,
		AreaCode areaCode,
		ContentType contentType,
		Season season,
		CountryRegion countryRegion,
		SortType sortType,
		Double latitude,
		Double longitude,
		String title
	){
		log.info("getThumbnailsByFilterAndSort called with page={}, size={}, areaCode={}, contentType={}, season={}, countryRegion={}, sortType={}, latitude={}, longitude={}, title='{}'",
			page, size, areaCode, contentType, season, countryRegion, sortType, latitude, longitude, title);
		if(sortType != SortType.DISTANCE_ASC){
			Pageable pageable = PageRequest.of(page, size, Sort.by(sortType.getDirection(),sortType.getColumnName()));

			Page<Place> placePage = (title== null || !title.isBlank()) ?  placeRepository.searchAndNotDistance(areaCode,title, contentType, season, countryRegion, pageable) :
				placeRepository.findFiltered(areaCode, contentType, season, countryRegion, pageable);
			List<PlaceThumbnailModel> thumbnails = placePage.getContent().stream()
				.map(place -> PlaceThumbnailModel.toPlaceThumbnailModel(
					place,  commonPlaceService.isBookmarked(user,place)
				))
				.toList();
			return PaginationModel.toPaginationModel(thumbnails,placePage);
		}
		else{
			Pageable pageable = PageRequest.of(page, size);
			Page<PlaceDistanceModel> placePage =
				(title== null || !title.isBlank()) ?  placeRepository.searchAndDistance(latitude,longitude, areaCode,title, contentType, season, countryRegion, pageable):
				placeRepository.findFilteredByDistance(latitude,longitude, areaCode, contentType, season, countryRegion, pageable);
			List<PlaceThumbnailModel> thumbnails = placePage.getContent().stream()
				.map(p -> PlaceThumbnailModel.toPlaceThumbnailModel(p,  // hashtags
					commonPlaceService.isBookmarked(user,p.getPlace())))
				.toList();
			return PaginationModel.toPaginationModel(thumbnails,placePage);
		}
	}

	public PlaceDetailInfoModel dd(User user,Long id,Double latitude,Double longitude){
		Place place = commonPlaceService.findById(id);

		List<InfoItemModel> infoItemModelList = korApiService.getDetailIntro(place.getContentId(),place.getContentType().getCode()).block();
		String overview = korApiService.getDetailOverview(place.getContentId());
		infoItemModelList.add(0, new InfoItemModel("개요", overview));
		return PlaceDetailInfoModel.builder()
			.id(id)
			.title(place.getTitle())
			.reviewScoreAverage(place.getReviewScoreAverage())
			.address(place.getAddr1())
			.contentTypeKoreanName(place.getContentType().getName())
			.distance(place.getLocation().distanceTo(latitude,longitude))
			.infoItemList(infoItemModelList)
			.latitude(place.getLocation().getMapx())
			.longitude(place.getLocation().getMapy())
			.isBookmarked(commonPlaceService.isBookmarked(user,place))
			.build();
	}



}
