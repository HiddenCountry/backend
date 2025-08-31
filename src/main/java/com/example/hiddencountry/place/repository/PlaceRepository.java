package com.example.hiddencountry.place.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.hiddencountry.place.domain.Place;
import com.example.hiddencountry.place.domain.type.AreaCode;
import com.example.hiddencountry.place.domain.type.Cat1;
import com.example.hiddencountry.place.domain.type.ContentType;
import com.example.hiddencountry.place.domain.type.CountryRegion;
import com.example.hiddencountry.place.domain.type.Season;
import com.example.hiddencountry.place.model.PlaceDistanceModel;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

	@Query("""
		SELECT p
		FROM Place p
		WHERE (:areaCode IS NULL OR p.areaCode = :areaCode)
		AND p.id in (
				select pc.id.placeId FROM PlaceCountry pc where pc.id.countryRegion = :countryRegion
		)
		AND (:contentType IS NULL OR p.contentType = :contentType)
		AND (:season IS NULL OR p.rSeason = :season)
    """)
	Page<Place> findFiltered(
		@Param("areaCode") AreaCode areaCode,
		@Param("contentType") ContentType contentType,
		@Param("season") Season season,
		@Param("countryRegion") CountryRegion countryRegion,
		Pageable pageable
	);

	@Query("""
    SELECT new com.example.hiddencountry.place.model.PlaceDistanceModel(
        p,
        CAST(
          (6371 * acos(
            cos(radians(:currLat)) * cos(radians(p.location.mapy)) *
            cos(radians(p.location.mapx) - radians(:currLng)) +
            sin(radians(:currLat)) * sin(radians(p.location.mapy))
          ))
        AS Double)
    )
    FROM Place p
    WHERE (:areaCode IS NULL OR p.areaCode = :areaCode)
      AND (:contentType IS NULL OR p.contentType = :contentType)
      AND (:season IS NULL OR p.rSeason = :season)
      AND p.id IN (
          SELECT pc.id.placeId FROM PlaceCountry pc WHERE pc.id.countryRegion = :countryRegion
      )
    ORDER BY 2 ASC
""")
	Page<PlaceDistanceModel> findFilteredByDistance(
			@Param("currLat") double currLat,
			@Param("currLng") double currLng,
			@Param("areaCode") AreaCode areaCode,
			@Param("contentType") ContentType contentType,
			@Param("season") Season season,
			@Param("countryRegion") CountryRegion countryRegion,
			Pageable pageable
	);

	@Query("""
    SELECT p
    FROM Place p
    WHERE (:areaCode IS NULL OR p.areaCode = :areaCode)
      AND (:title IS NULL OR p.title LIKE %:title%)
      AND p.id IN (
          SELECT pc.id.placeId FROM PlaceCountry pc WHERE pc.id.countryRegion = :countryRegion
      )
      AND (:contentType IS NULL OR p.contentType = :contentType)
      AND (:season IS NULL OR p.rSeason = :season)
	""")
	Page<Place> searchAndNotDistance(
		@Param("areaCode") AreaCode areaCode,
		@Param("title") String title,
		@Param("contentType") ContentType contentType,
		@Param("season") Season season,
		@Param("countryRegion") CountryRegion countryRegion,
		Pageable pageable
	);

	@Query("""
    SELECT new com.example.hiddencountry.place.model.PlaceDistanceModel(
        p,
        CAST(
          (6371 * acos(
            cos(radians(:currLat)) * cos(radians(p.location.mapy)) *
            cos(radians(p.location.mapx) - radians(:currLng)) +
            sin(radians(:currLat)) * sin(radians(p.location.mapy))
          ))
        AS Double)
    )
    FROM Place p
    WHERE (:areaCode IS NULL OR p.areaCode = :areaCode)
      AND (:title IS NULL OR p.title LIKE %:title%)
      AND (:contentType IS NULL OR p.contentType = :contentType)
      AND (:season IS NULL OR p.rSeason = :season)
      AND p.id IN (
          SELECT pc.id.placeId FROM PlaceCountry pc WHERE pc.id.countryRegion = :countryRegion
      )
    ORDER BY 2 ASC
	""")
	Page<PlaceDistanceModel> searchAndDistance(
		@Param("currLat") double currLat,
		@Param("currLng") double currLng,
		@Param("areaCode") AreaCode areaCode,
		@Param("title") String title,
		@Param("contentType") ContentType contentType,
		@Param("season") Season season,
		@Param("countryRegion") CountryRegion countryRegion,
		Pageable pageable
	);

}
