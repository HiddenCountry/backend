package com.example.hiddencountry.place.domain.type;

import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SortType {

	REVIEW_COUNT_DESC("reviewCount",Sort.Direction.DESC),
	DISTANCE_ASC("distance",Sort.Direction.DESC),
	REVIEW_SCORE_AVERAGE_DESC("reviewScoreAverage",Sort.Direction.DESC),
	VIEW_COUNT_DESC("viewCount",Sort.Direction.DESC);

	final String columnName;
	final Sort.Direction direction;

}
