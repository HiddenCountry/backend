package com.example.hiddencountry.review.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.hiddencountry.review.domain.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 최신순(id DESC): 첫 페이지
    @EntityGraph(attributePaths = {"images"})
    Slice<Review> findByPlace_IdOrderByIdDesc(Long placeId, Pageable pageable);

    // 최신순(id DESC): 다음 페이지
    @EntityGraph(attributePaths = {"images"})
    Slice<Review> findByPlace_IdAndIdLessThanOrderByIdDesc(Long placeId, Long id, Pageable pageable);

    // 평점순(score DESC, id DESC): 첫 페이지
    @EntityGraph(attributePaths = {"images"})
    Slice<Review> findByPlace_IdOrderByScoreDescIdDesc(Long placeId, Pageable pageable);

    // 평점순: 커서 이후 (score,id)
    @EntityGraph(attributePaths = {"images"})
    @Query("""
        select r from Review r
        where r.place.id = :placeId
          and (r.score < :score or (r.score = :score and r.id < :id))
        order by r.score desc, r.id desc
    """)
    Slice<Review> findTopRatedAfter(
            @Param("placeId") Long placeId,
            @Param("score") Integer score,
            @Param("id") Long id,
            Pageable pageable
    );

    @Query("""
    select coalesce(avg(r.score), 0)
    from Review r
    where r.place.id = :placeId and r.score is not null
""")
    Double avgScoreByPlaceId(@Param("placeId") Long placeId);

    long countByPlace_Id(Long placeId);
}
