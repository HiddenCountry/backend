package com.example.hiddencountry.place.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hiddencountry.place.domain.UserPlace;
import com.example.hiddencountry.place.domain.UserPlaceId;

@Repository
public interface UserPlaceRepository extends JpaRepository<UserPlace, UserPlaceId> {
}
