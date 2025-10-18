package com.example.hiddencountry.place.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class TravelCourseCreateRequest {

	private String name;

	// 순서대로 들어온 Place ID 리스트
	private List<Long> placeIds;

}