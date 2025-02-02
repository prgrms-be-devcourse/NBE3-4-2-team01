package com.ll.hotel.domain.review.comment.service;

import com.ll.hotel.domain.review.comment.dto.ReviewCommentDto;
import com.ll.hotel.domain.review.comment.entity.ReviewComment;
import com.ll.hotel.domain.review.comment.repository.ReviewCommentRepository;
import com.ll.hotel.domain.review.comment.type.ReviewCommentStatus;
import com.ll.hotel.domain.review.review.entity.Review;
import com.ll.hotel.global.exceptions.ServiceException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewCommentService {
    private final ReviewCommentRepository reviewCommentRepository;
    private final EntityManager entityManager;

    public ReviewComment createReviewComment(long reviewId, String content) {
        ReviewComment reviewComment = ReviewComment.builder()
                .review(entityManager.getReference(Review.class, reviewId))
                .content(content)
                .reviewCommentStatus(ReviewCommentStatus.CREATED)
                .build();

        return reviewCommentRepository.save(reviewComment);
    }

    public void updateReviewComment(long reviewCommentId, String content) {
        ReviewComment reviewComment = reviewCommentRepository.findByIdWithFilter(reviewCommentId)
                .orElseThrow(() -> new ServiceException("400-1", "수정할 리뷰 답변이 존재하지 않습니다."));

        reviewComment.setContent(content);
        reviewComment.setReviewCommentStatus(ReviewCommentStatus.UPDATED);
    }

    public void deleteReviewComment(long reviewCommentId) {
        ReviewComment reviewComment = reviewCommentRepository.findByIdWithFilter(reviewCommentId)
                .orElseThrow(() -> new ServiceException("400-1", "삭제할 리뷰 답변이 존재하지 않습니다."));
        reviewComment.setReviewCommentStatus(ReviewCommentStatus.DELETED);
    }

    public ReviewCommentDto getReviewComment(long reviewCommentId) {
        ReviewComment reviewComment = reviewCommentRepository.findByIdWithFilter(reviewCommentId)
                .orElseThrow(() -> new ServiceException("400-1", "존재하지 않는 리뷰 답변입니다."));

        return new ReviewCommentDto(reviewComment);
    }
}
