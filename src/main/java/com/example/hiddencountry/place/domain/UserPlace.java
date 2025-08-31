package com.example.hiddencountry.place.domain;

import com.example.hiddencountry.user.domain.User;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_place")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPlace {

	@EmbeddedId
	private UserPlaceId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userId")
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("placeId")
	@JoinColumn(name = "place_id", nullable = false)
	private Place place;

	public static UserPlace of(User user, Place place) {
		UserPlace up = new UserPlace();
		up.setUser(user);
		up.setPlace(place);
		up.setId(new UserPlaceId(user.getId(), place.getId()));
		return up;
	}

}

