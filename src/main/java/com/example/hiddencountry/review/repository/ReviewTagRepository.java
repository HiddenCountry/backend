package com.example.hiddencountry.review.repository;

import com.example.hiddencountry.review.domain.ReviewTag;
import com.example.hiddencountry.review.domain.type.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewTagRepository extends JpaRepository<ReviewTag, Long> {

    @Query("""
        select t.tag
        from ReviewTag t
        where t.review.place.id = :placeId
        group by t.tag
        order by count(t.id) desc
    """)
    List<Tag> findTopTagValuesByPlace(@Param("placeId") Long placeId, Pageable pageable);
}
