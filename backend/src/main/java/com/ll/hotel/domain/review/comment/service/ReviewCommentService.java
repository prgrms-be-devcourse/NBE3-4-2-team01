package com.ll.hotel.domain.review.comment.service;

import com.ll.hotel.domain.hotel.hotel.repository.HotelRepository;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.review.comment.dto.ReviewCommentDto;
import com.ll.hotel.domain.review.comment.entity.ReviewComment;
import com.ll.hotel.domain.review.comment.repository.ReviewCommentRepository;
import com.ll.hotel.domain.review.review.entity.Review;
import com.ll.hotel.domain.review.review.repository.ReviewRepository;
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
    private final HotelRepository hotelRepository;
    private final ReviewRepository reviewRepository;
    private final EntityManager entityManager;

    public ReviewComment createReviewComment(Member actor, long reviewId, String content) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ServiceException("400-1", "리뷰가 존재하지 않습니다."));

        ReviewComment reviewComment = ReviewComment.builder()
                .review(review)
                .content(content)
                .build();

        if(!review.getHotel().isOwnedBy(actor)) {
            throw new ServiceException("403-1", "이 호텔 사업자만 리뷰 답변 작성이 가능합니다.");
        }

        return reviewCommentRepository.save(reviewComment);
    }

    public void updateReviewComment(Member actor, long reviewCommentId, String content) {
        ReviewComment reviewComment = reviewCommentRepository.findById(reviewCommentId)
                .orElseThrow(() -> new ServiceException("400-1", "수정할 리뷰 답변이 존재하지 않습니다."));

        if(!reviewComment.getReview().getHotel().isOwnedBy(actor)) {
            throw new ServiceException("403-1", "답변 작성자만 리뷰 답변 수정 가능합니다.");
        }

        reviewComment.setContent(content);
    }

    public void deleteReviewComment(Member actor, long reviewCommentId) {
        ReviewComment reviewComment = reviewCommentRepository.findById(reviewCommentId)
                .orElseThrow(() -> new ServiceException("400-1", "삭제할 리뷰 답변이 존재하지 않습니다."));

        if(!reviewComment.getReview().getHotel().isOwnedBy(actor)) {
            throw new ServiceException("403-1", "답변 작성자만 리뷰 답변 삭제 가능합니다.");
        }

        reviewComment.getReview().setReviewComment(null);
    }

    public ReviewCommentDto getReviewComment(long reviewCommentId) {
        ReviewComment reviewComment = reviewCommentRepository.findById(reviewCommentId)
                .orElseThrow(() -> new ServiceException("400-1", "존재하지 않는 리뷰 답변입니다."));

        return new ReviewCommentDto(reviewComment);
    }
}
