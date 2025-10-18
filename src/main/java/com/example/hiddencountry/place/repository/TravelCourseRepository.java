package com.example.hiddencountry.place.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hiddencountry.place.domain.TravelCourse;

public interface TravelCourseRepository extends JpaRepository<TravelCourse, Long> {
}