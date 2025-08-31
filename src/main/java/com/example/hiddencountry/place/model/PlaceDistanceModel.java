package com.example.hiddencountry.place.model;

import com.example.hiddencountry.place.domain.Place;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceDistanceModel {
	private Place place;
	private Double distance;
}
