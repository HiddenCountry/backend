package com.example.hiddencountry.place.domain;

import java.io.Serializable;

import com.example.hiddencountry.place.domain.type.CountryRegion;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PlaceCountryId implements Serializable {

	@Column(name = "place_id")
	private Long placeId;

	@Enumerated(EnumType.STRING)
	@Column(name = "country_region", length = 20)
	private CountryRegion countryRegion;

}

