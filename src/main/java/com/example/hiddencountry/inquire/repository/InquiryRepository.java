package com.example.hiddencountry.inquire.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hiddencountry.inquire.domain.Inquiry;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry,Long> {
}
