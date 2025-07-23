package com.example.hiddencountry.entity.place.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hiddencountry.entity.place.domain.PlaceCountry;
import com.example.hiddencountry.entity.place.domain.UserPlace;
import com.example.hiddencountry.entity.place.domain.UserPlaceId;

@Repository
public interface UserPlaceRepository extends JpaRepository<UserPlace, UserPlaceId> {
}
