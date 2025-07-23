package com.example.hiddencountry.entity.place.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hiddencountry.entity.place.domain.Place;
import com.example.hiddencountry.entity.place.domain.PlaceCountry;

@Repository
public interface PlaceCountryRepository extends JpaRepository<PlaceCountry,Long> {
}
