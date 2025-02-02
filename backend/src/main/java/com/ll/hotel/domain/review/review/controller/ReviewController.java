package com.ll.hotel.domain.review.review.controller;

import com.ll.hotel.domain.image.service.ImageService;
import com.ll.hotel.domain.image.type.ImageType;
import com.ll.hotel.domain.review.review.dto.PostReviewRequest;
import com.ll.hotel.domain.review.review.dto.PresignedUrlsResponse;
import com.ll.hotel.domain.review.review.service.ReviewService;
import com.ll.hotel.global.aws.s3.S3Service;
import com.ll.hotel.global.rsData.RsData;
import com.ll.hotel.standard.base.Empty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Transactional
@Tag(name = "ReviewController")
public class ReviewController {

    private final ImageService imageService;
    private final ReviewService reviewService;
    private final S3Service s3Service;

    @PostMapping("/{bookingId}")
    @Operation(summary = "리뷰 생성")
    public RsData<PresignedUrlsResponse> createReview(
            @PathVariable("bookingId") Long bookingId,
            @RequestParam("hotelId") Long hotelId, // 임시
            @RequestParam("roomId") Long roomId, // 임시
            @RequestParam("memberId") Long memberId, // 임시
            @RequestBody PostReviewRequest postReviewRequest) {
        // 인증 체크 (로그인된 사용자인가?)

        // 권한 체크 (예약자가 맞는가?)


        // 리뷰 생성
        long reviewId = reviewService.createReview(hotelId, roomId, memberId, bookingId,
                postReviewRequest.content(), postReviewRequest.rating());

        // 리뷰 사진 저장
        List<String> extensions = Optional.ofNullable(postReviewRequest.imageExtensions())
                .orElse(Collections.emptyList());
        List<URL> urls = s3Service.generatePresignedUrls(ImageType.REVIEW, reviewId, extensions);

        return new RsData<>(
                "200-1",
                "리뷰가 생성되었습니다.",
                new PresignedUrlsResponse(reviewId, urls));
    }

    @PostMapping("/{reviewId}/urls")
    @Operation(summary = "사진 URL 리스트 저장")
    public RsData<Empty> saveImageUrls(
            @PathVariable("reviewId") long reviewId,
            @RequestBody List<String> urls) {


        imageService.saveImages(ImageType.REVIEW, reviewId, urls);
        return RsData.OK;
    }

}
