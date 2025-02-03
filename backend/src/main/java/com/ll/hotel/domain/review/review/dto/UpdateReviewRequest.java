package com.ll.hotel.domain.review.review.dto;

import java.util.List;

public record UpdateReviewRequest(
        String content,
        Integer rating,
        List<String> deleteImageUrls,
        List<String> newImageExtensions
) {
}
