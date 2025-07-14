package com.example.hiddencountry.entity.place.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hiddencountry.entity.place.domain.Place;


@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

}
