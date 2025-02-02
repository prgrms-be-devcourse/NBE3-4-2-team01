package com.ll.hotel.domain.review.review.dto;

import java.util.List;

public record PostReviewRequest(
        String content,
        Integer rating,
        List<String> imageExtensions
) {
}
