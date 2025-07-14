package com.example.hiddencountry.entity.place.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hiddencountry.entity.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
