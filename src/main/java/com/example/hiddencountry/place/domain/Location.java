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


	public Long distanceTo(Double latitude, Double longitude) {
		if (latitude == null || longitude == null) { return null; }
		return calculateDistance(this.mapy, this.mapx, latitude, longitude);
	}

	/**
	 * 두 좌표(위도, 경도) 간의 거리를 m 단위로 계산
	 */
	public static Long calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
		if (lat1 == null || lon1 == null || lat2 == null || lon2 == null) { return null; }

		final int R = 6371; // 지구 반지름 (km)

		double lat1Rad = Math.toRadians(lat1);
		double lon1Rad = Math.toRadians(lon1);
		double lat2Rad = Math.toRadians(lat2);
		double lon2Rad = Math.toRadians(lon2);

		double dLat = lat2Rad - lat1Rad;
		double dLon = lon2Rad - lon1Rad;

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
			Math.cos(lat1Rad) * Math.cos(lat2Rad) *
				Math.sin(dLon / 2) * Math.sin(dLon / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		double distance = R * c; // km
		return Math.round(distance * 1000); // m 단위로 반환
	}
}