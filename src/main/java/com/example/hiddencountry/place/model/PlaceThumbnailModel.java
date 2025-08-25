package com.example.hiddencountry.place.model;

import java.util.List;

import com.example.hiddencountry.place.domain.Place;
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

	public static PlaceThumbnailModel toPlaceThumbnailModel(Place place,List<String> hashtags,Boolean isBookmarked){
		return PlaceThumbnailModel.builder()
			.id(place.getId())
			.firstImage(place.getFirstImage())
			.contendId(place.getContentId())
			.addr1(place.getAddr1())
			.reviewScoreAverage(place.getReviewScoreAverage())
			.reviewCount(place.getReviewCount())
			.season(place.getRSeason())
			.hashtags(hashtags)
			.isBookmarked(isBookmarked).build();
	}

	public static PlaceThumbnailModel toPlaceThumbnailModel(PlaceDistanceModel p,List<String> hashtags,Boolean isBookmarked) {
		var place = p.getPlace();
		System.out.println(p.getPlace().getAddr1() + " - " +p.getDistance());
		return PlaceThumbnailModel.builder()
			.id(place.getId())
			.firstImage(place.getFirstImage())
			.contendId(place.getContentId())
			.addr1(place.getAddr1())
			.reviewScoreAverage(place.getReviewScoreAverage())
			.reviewCount(place.getReviewCount())
			.season(place.getRSeason())
			.hashtags(hashtags)
			.isBookmarked(isBookmarked)
			.build();
	}
}
