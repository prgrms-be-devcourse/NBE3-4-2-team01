package com.ll.hotel.domain.review.review.dto;

import java.util.List;

public record GetReviewResponse(
        ReviewDto reviewDto,
        List<String> imageUrls
) {
}
