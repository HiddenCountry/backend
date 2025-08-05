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
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@Table(name = "place_country")
@AllArgsConstructor
@NoArgsConstructor
public class PlaceCountry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "place_id", referencedColumnName = "id", nullable = false)
	@NotNull
	private Place place;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "country_region", nullable = false, length = 20)
	private CountryRegion countryRegion;

}
