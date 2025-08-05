package com.example.hiddencountry.place.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {

	@Column(name = "mapx", nullable = false)
	private Double mapx;

	@Column(name = "mapy", nullable = false)
	private Double mapy;

	public int distanceTo(Location other) {
		// 거리 계산 로직
		return 0;
	}
}