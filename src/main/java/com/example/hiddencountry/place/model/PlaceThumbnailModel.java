package com.example.hiddencountry.place.model;

import java.util.ArrayList;
import java.util.List;

import com.example.hiddencountry.place.domain.Location;
import com.example.hiddencountry.place.domain.Place;
import com.example.hiddencountry.place.domain.type.ContentType;
import com.example.hiddencountry.place.domain.type.Season;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PlaceThumbnailModel {

	Long id;
	String firstImage;
	Long contentId;
	float reviewScoreAverage;
	Long reviewCount;
	String addr1;
	Season season;
	List<String> hashtags;
	Boolean isBookmarked;
	String title;
	ContentType contentTypeName;
	int contentTypeId;
	Double longitude;
	Double latitude;

	// 프론트 구현 편리를 위해, 한국어 이름, 거리도 함께 반환
	String contentTypeKoreanName;
	List<String> countryRegionKoreanNames;
	Long distance;

	public static PlaceThumbnailModel toPlaceThumbnailModel(Place place,Boolean isBookmarked,Double userLatitude,Double userLongitude) {
		List<String> hashtags = new ArrayList<>();
		if (place.getTopHashtag1() != null) {
			hashtags.add(place.getTopHashtag1().getDescription());
		}
		if (place.getTopHashtag2() != null) {
			hashtags.add(place.getTopHashtag2().getDescription());
		}


		return PlaceThumbnailModel.builder()
			.id(place.getId())
			.firstImage(place.getFirstImage())
			.contentId(place.getContentId())
			.addr1(place.getAddr1())
			.reviewScoreAverage(place.getReviewScoreAverage())
			.reviewCount(place.getReviewCount())
			.season(place.getRSeason())
			.hashtags(hashtags)
			.title(place.getTitle())
			.isBookmarked(isBookmarked)
			.contentTypeName(place.getContentType())
			.contentTypeId(place.getContentType().getCode())
			.contentTypeKoreanName(place.getContentType().getName())
			.countryRegionKoreanNames(place.getPlaceCountries().stream().map(pc->pc.getId().getCountryRegion().getDisplayName()).toList())
			.longitude(place.getLocation().getMapx())
			.latitude(place.getLocation().getMapy())
			.distance(calculateDistanceSafe(userLatitude,userLongitude,place.getLocation()))
			.build();
	}

	// public static PlaceThumbnailModel toPlaceThumbnailModel(PlaceDistanceModel p,Boolean isBookmarked) {
	// 	var place = p.getPlace();
	// 	List<String> hashtags = new ArrayList<>();
	// 	if (place.getTopHashtag1() != null) {
	// 		hashtags.add(place.getTopHashtag1().getDescription());
	// 	}
	// 	if (place.getTopHashtag2() != null) {
	// 		hashtags.add(place.getTopHashtag2().getDescription());
	// 	}
	// 	return PlaceThumbnailModel.builder()
	// 		.id(place.getId())
	// 		.firstImage(place.getFirstImage())
	// 		.contentId(place.getContentId())
	// 		.addr1(place.getAddr1())
	// 		.reviewScoreAverage(place.getReviewScoreAverage())
	// 		.reviewCount(place.getReviewCount())
	// 		.season(place.getRSeason())
	// 		.hashtags(hashtags)
	// 		.title(place.getTitle())
	// 		.contentTypeName(place.getContentType())
	// 		.contentTypeId(place.getContentType().getCode())
	// 		.isBookmarked(isBookmarked)
	// 		.contentTypeKoreanName(place.getContentType().getName())
	// 		.countryRegionKoreanNames(place.getPlaceCountries().stream().map(pc->pc.getId().getCountryRegion().getDisplayName()).toList())
	// 		.longitude(place.getLocation().getMapx())
	// 		.latitude(place.getLocation().getMapy())
	// 		.build();
	// }

	public static Long calculateDistanceSafe(Double userLatitude,Double userLongitude,
		Location placeLocation) {
		return placeLocation.distanceTo(userLatitude,userLongitude);
	}
}
