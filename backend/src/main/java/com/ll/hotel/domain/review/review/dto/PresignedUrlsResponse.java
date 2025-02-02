package com.ll.hotel.domain.review.review.dto;

import java.net.URL;
import java.util.List;

public record PresignedUrlsResponse(
        long reviewId,
        List<URL> presignedUrls
) {
}
