package com.example.hiddencountry.place.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TravelCourseDetailModel {
	private Long courseId;
	private String title;
	private String firstImage;
	private List<PlaceThumbnailModel> places;
}
