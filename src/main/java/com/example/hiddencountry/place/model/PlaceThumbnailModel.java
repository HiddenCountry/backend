package com.example.hiddencountry.place.model;

import java.util.ArrayList;
import java.util.List;

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
	Long contendId;
	float reviewScoreAverage;
	Long reviewCount;
	String addr1;
	Season season;
	List<String> hashtags;
	Boolean isBookmarked;
	String title;
	ContentType contentTypeName;
	int contentTypeId;

	public static PlaceThumbnailModel toPlaceThumbnailModel(Place place,Boolean isBookmarked){
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
			.contendId(place.getContentId())
			.addr1(place.getAddr1())
			.reviewScoreAverage(place.getReviewScoreAverage())
			.reviewCount(place.getReviewCount())
			.season(place.getRSeason())
			.hashtags(hashtags)
			.title(place.getTitle())
			.isBookmarked(isBookmarked)
			.contentTypeName(place.getContentType())
			.contentTypeId(place.getContentType().getCode())
			.build();
	}

	public static PlaceThumbnailModel toPlaceThumbnailModel(PlaceDistanceModel p,Boolean isBookmarked) {
		var place = p.getPlace();
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
			.contendId(place.getContentId())
			.addr1(place.getAddr1())
			.reviewScoreAverage(place.getReviewScoreAverage())
			.reviewCount(place.getReviewCount())
			.season(place.getRSeason())
			.hashtags(hashtags)
			.title(place.getTitle())
			.contentTypeName(place.getContentType())
			.contentTypeId(place.getContentType().getCode())
			.isBookmarked(isBookmarked)
			.build();
	}
}
