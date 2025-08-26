package com.example.hiddencountry.place.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hiddencountry.place.domain.Place;
import com.example.hiddencountry.place.domain.UserPlace;
import com.example.hiddencountry.place.domain.UserPlaceId;
import com.example.hiddencountry.user.domain.User;

@Repository
public interface UserPlaceRepository extends JpaRepository<UserPlace, UserPlaceId> {
	boolean existsByUserAndPlace(User user, Place place);

	Optional<UserPlace> findByUserAndPlace(User user, Place place);
}
