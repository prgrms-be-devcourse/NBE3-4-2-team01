package com.ll.hotel.domain.review.review.dto;

import com.ll.hotel.domain.review.comment.dto.ReviewCommentDto;
import com.ll.hotel.domain.review.comment.entity.ReviewComment;
import com.ll.hotel.domain.review.review.entity.Review;

import java.time.LocalDateTime;

public record MyReviewWithCommentDto(
        String hotelName,
        String roomTypeName,
        ReviewDto reviewDto,
        ReviewCommentDto reviewCommentDto,
        LocalDateTime reservedAt
) {
    public MyReviewWithCommentDto(String hotelName, String roomTypeName, Review review, ReviewComment reviewComment, LocalDateTime reservedAt) {
        this(
                hotelName,
                roomTypeName,
                new ReviewDto(review),
                reviewComment != null ? new ReviewCommentDto(reviewComment) : null,
                reservedAt
        );
    }
}
