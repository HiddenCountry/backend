package com.example.hiddencountry.place.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;

import com.example.hiddencountry.global.pagination.PaginationModel;
import com.example.hiddencountry.place.domain.Place;
import com.example.hiddencountry.place.domain.type.Cat1;
import com.example.hiddencountry.place.domain.type.ContentType;
import com.example.hiddencountry.place.domain.type.CountryRegion;
import com.example.hiddencountry.place.domain.type.Season;
import com.example.hiddencountry.place.domain.type.SortType;
import com.example.hiddencountry.place.model.PlaceDistanceModel;
import com.example.hiddencountry.place.model.PlaceThumbnailModel;
import com.example.hiddencountry.place.repository.PlaceRepository;
import com.example.hiddencountry.user.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceService {

	private final PlaceRepository placeRepository;

	public PaginationModel<PlaceThumbnailModel> getThumbnailsByFilterAndSort(
		User user,
		Integer page,
		Integer size,
		Cat1 cat1,
		ContentType contentType,
		Season season,
		CountryRegion countryRegion,
		SortType sortType,
		Double latitude,
		Double longitude
	){
		if(sortType != SortType.DISTANCE_ASC){
			Pageable pageable = PageRequest.of(page, size, Sort.by(sortType.getDirection(),sortType.getColumnName()));
			Page<Place> placePage = placeRepository.findFiltered(cat1, contentType, season, countryRegion, pageable);
			List<PlaceThumbnailModel> thumbnails = placePage.getContent().stream()
				.map(place -> PlaceThumbnailModel.toPlaceThumbnailModel(
					place,  List.of(), false
				))
				.toList();
			return PaginationModel.toPaginationModel(thumbnails,placePage);
		}
		else{
			Pageable pageable = PageRequest.of(page, size);
			Page<PlaceDistanceModel> placePage = placeRepository.findFilteredByDistance(latitude,longitude, cat1, contentType, season, countryRegion, pageable);
			List<PlaceThumbnailModel> thumbnails = placePage.getContent().stream()
				.map(p -> PlaceThumbnailModel.toPlaceThumbnailModel(p, List.of(), // hashtags
					false))
				.toList();
			return PaginationModel.toPaginationModel(thumbnails,placePage);
		}
	}


}
