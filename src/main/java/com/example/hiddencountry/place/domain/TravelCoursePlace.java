package com.example.hiddencountry.place.domain;

import com.example.hiddencountry.global.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TravelCoursePlace extends BaseEntity {

	@EmbeddedId
	private TravelCoursePlaceId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("travelCourseId")
	private TravelCourse travelCourse;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("placeId")
	private Place place;

	@Column(nullable = false)
	private int orderIndex; // 여행 순서

	public TravelCoursePlace(TravelCourse travelCourse, Place place, int orderIndex) {
		this.id = new TravelCoursePlaceId(travelCourse.getId(), place.getId());
		this.travelCourse = travelCourse;
		this.place = place;
		this.orderIndex = orderIndex;
	}
}