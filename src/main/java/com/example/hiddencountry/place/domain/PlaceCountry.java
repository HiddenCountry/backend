package com.example.hiddencountry.place.domain;

import com.example.hiddencountry.place.domain.type.CountryRegion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.EmbeddedId;

@Getter
@Builder
@Entity
@Table(name = "place_country")
@AllArgsConstructor
@NoArgsConstructor
public class PlaceCountry {

	@EmbeddedId
	private PlaceCountryId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("placeId") // 복합키 중 하나인 placeId에 매핑
	@JoinColumn(name = "place_id", nullable = false)
	private Place place;

}
