package com.example.hiddencountry.place.model;

import java.util.List;

import com.example.hiddencountry.place.domain.Location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PlaceDetailInfoModel {
	Long id;
	String title;
	Float reviewScoreAverage;
	String address;
	String contentTypeKoreanName;
	Long distance;
	List<InfoItemModel> infoItemList;
	Double latitude;
	Double longitude;
	Boolean isBookmarked;
	Boolean isExoticPlace;

	/**
	 * 거리 계산과 null-safe 처리를 포함하여 PlaceDetailInfoModel 생성
	 */
	public static PlaceDetailInfoModel of(
		Long id,
		String title,
		Float reviewScoreAverage,
		String address,
		String contentTypeKoreanName,
		List<InfoItemModel> infoItemList,
		Double latitude,
		Double longitude,
		Double refLatitude,
		Double refLongitude,
		Boolean isBookmarked,
		Boolean isExoticPlace
	) {
		Long distance = null;
		if (latitude != null && longitude != null && refLatitude != null && refLongitude != null) {
			distance = Location.calculateDistance(refLatitude, refLongitude, latitude, longitude);
		}

		return PlaceDetailInfoModel.builder()
			.id(id)
			.title(title)
			.reviewScoreAverage(reviewScoreAverage)
			.address(address)
			.contentTypeKoreanName(contentTypeKoreanName)
			.distance(distance)
			.infoItemList(infoItemList)
			.latitude(latitude)
			.longitude(longitude)
			.isBookmarked(isBookmarked)
			.isExoticPlace(isExoticPlace != null && isExoticPlace)
			.build();
	}
}
