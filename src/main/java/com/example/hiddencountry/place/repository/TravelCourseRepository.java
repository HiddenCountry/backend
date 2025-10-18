package com.example.hiddencountry.place.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hiddencountry.place.domain.TravelCourse;
import com.example.hiddencountry.user.domain.User;

@Repository
public interface TravelCourseRepository extends JpaRepository<TravelCourse, Long> {
	List<TravelCourse> findByUser(User user);
}