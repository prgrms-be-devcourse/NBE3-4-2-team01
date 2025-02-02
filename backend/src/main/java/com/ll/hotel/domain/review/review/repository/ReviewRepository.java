package com.ll.hotel.domain.review.review.repository;

import com.ll.hotel.domain.review.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // DELETED 가 아닌 것만 조회
    @Query("SELECT r FROM Review r WHERE r.id = :reviewId AND r.reviewStatus <> 'DELETED'")
    Optional<Review> findByIdWithFilter(@Param("reviewId") Long reviewId);

}
