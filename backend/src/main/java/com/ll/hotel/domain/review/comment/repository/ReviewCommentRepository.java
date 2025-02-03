package com.ll.hotel.domain.review.comment.repository;

import com.ll.hotel.domain.review.comment.entity.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {
    // DELETED 가 아닌 것만 조회
    @Query("SELECT rc FROM ReviewComment rc WHERE rc.id = :reviewCommentId AND rc.reviewCommentStatus <> 'DELETED'")
    Optional<ReviewComment> findByIdWithFilter(@Param("reviewCommentId") Long reviewCommentId);

}
