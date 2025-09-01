package com.example.hiddencountry.place.model;

import java.util.List;

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
	float reviewScoreAverage;
	String address;
	String contentTypeKoreanName;
	Long distance;
	List<InfoItemModel> infoItemList;
	Double latitude;
	Double longitude;
	Boolean isBookmarked;
}
