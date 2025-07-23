package com.example.hiddencountry.entity.place.domain;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPlaceId implements Serializable {

	private Long userId;
	private Long placeId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof UserPlaceId)) return false;
		UserPlaceId that = (UserPlaceId) o;
		return Objects.equals(userId, that.userId) &&
			Objects.equals(placeId, that.placeId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, placeId);
	}
}
