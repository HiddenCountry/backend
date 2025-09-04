package com.example.hiddencountry.place.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hiddencountry.place.domain.PlaceCountry;

@Repository
public interface PlaceCountryRepository extends JpaRepository<PlaceCountry,Long> {
}
