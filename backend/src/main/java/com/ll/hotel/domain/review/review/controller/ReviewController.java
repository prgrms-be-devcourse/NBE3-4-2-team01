package com.ll.hotel.domain.review.review.controller;

import com.ll.hotel.domain.hotel.hotel.service.HotelService;
import com.ll.hotel.domain.image.service.ImageService;
import com.ll.hotel.domain.image.type.ImageType;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.review.review.dto.request.PostReviewRequest;
import com.ll.hotel.domain.review.review.dto.request.UpdateReviewRequest;
import com.ll.hotel.domain.review.review.dto.response.*;
import com.ll.hotel.domain.review.review.service.ReviewService;
import com.ll.hotel.global.aws.s3.S3Service;
import com.ll.hotel.global.exceptions.ServiceException;
import com.ll.hotel.global.rq.Rq;
import com.ll.hotel.global.rsData.RsData;
import com.ll.hotel.standard.base.Empty;
import com.ll.hotel.standard.page.dto.PageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final HotelService hotelService;
    private final ReviewService reviewService;
    private final S3Service s3Service;
    private final Rq rq;

    @PostMapping("/{bookingId}")
    @Operation(summary = "리뷰 생성")
    public RsData<PresignedUrlsResponse> createReview(
            @PathVariable("bookingId") Long bookingId,
            @RequestParam("hotelId") Long hotelId,
            @RequestParam("roomId") Long roomId,
            @RequestBody @Valid PostReviewRequest postReviewRequest) {
        Member actor = rq.getActor();

        // 리뷰 생성 (+권한 체크)
        long reviewId = reviewService.createReview(hotelId, roomId, actor.getId(), bookingId,
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
        Member actor = rq.getActor();

        // 권한 체크 (리뷰 작성자인가?)
        if (!reviewService.getReview(reviewId).isWrittenBy(actor)) {
            throw new ServiceException("403-1", "리뷰 작성자만 사진 등록이 가능합니다.");
        }

        imageService.saveImages(ImageType.REVIEW, reviewId, urls);
        return RsData.OK;
    }

    @PutMapping("/{reviewId}")
    @Operation(summary = "리뷰 수정")
    public RsData<PresignedUrlsResponse> updateReview(
            @PathVariable("reviewId") long reviewId,
            @RequestBody @Valid UpdateReviewRequest updateReviewRequest
    ) {
        Member actor = rq.getActor();

        // content, rating 수정 (+권한 체크)
        reviewService.updateReviewContentAndRating(actor, reviewId, updateReviewRequest.content(), updateReviewRequest.rating());

        List<String> deleteImageUrls = Optional.ofNullable(updateReviewRequest.deleteImageUrls())
                .orElse(Collections.emptyList());

        // DB 사진 삭제
        imageService.deleteImagesByIdAndUrls(ImageType.REVIEW, reviewId, deleteImageUrls);
        // S3 사진 삭제
        s3Service.deleteObjectsByUrls(deleteImageUrls);

        List<String> extensions = Optional.ofNullable(updateReviewRequest.newImageExtensions())
                .orElse(Collections.emptyList());

        // 새로운 사진의 Presigned URL 반환
        List<URL> urls = s3Service.generatePresignedUrls(ImageType.REVIEW, reviewId, extensions);

        return new RsData<>(
                "200-1",
                "리뷰가 업데이트 되었습니다.",
                new PresignedUrlsResponse(reviewId, urls)
        );
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "리뷰 삭제")
    public RsData<Empty> deleteReview(
            @PathVariable("reviewId") long reviewId
    ) {
        Member actor = rq.getActor();

        // 리뷰 삭제 (+권한 체크)
        reviewService.deleteReview(actor, reviewId);
        // DB 의 사진 URL 정보 삭제
        long imageCount = imageService.deleteImages(ImageType.REVIEW, reviewId);
        // S3 의 사진 삭제
        if(imageCount > 0) {
            s3Service.deleteAllObjectsById(ImageType.REVIEW, reviewId);
        }

        return RsData.OK;
    }

    @GetMapping("/{reviewId}")
    @Operation(summary = "리뷰 수정하기 전 기존 정보 제공")
    public RsData<GetReviewResponse> getReview(
            @PathVariable("reviewId") long reviewId
    ) {
        Member actor = rq.getActor();

        return new RsData<>(
                "200-1",
                "리뷰 조회 성공",
                reviewService.getReviewResponse(actor, reviewId) // (+권한 체크)
        );
    }

    @GetMapping("/me")
    @Operation(summary = "내 리뷰 목록 조회")
    public RsData<PageDto<MyReviewResponse>> getMyReviews(
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        Member actor = rq.getActor();

        // 접속자 리뷰 목록 조회 (+ 권한 체크)
        Page<MyReviewResponse> myReviewPage = reviewService.getMyReviewResponses(actor, page);

        return new RsData<>(
                "200-1",
                "나의 리뷰 목록 생성",
                new PageDto<>(myReviewPage)
        );
    }

    @GetMapping("/hotels/{hotelId}")
    @Operation(summary = "호텔 리뷰 목록 조회")
    public RsData<HotelReviewListResponse> getHotelReviews(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @PathVariable("hotelId") long hotelId
    ) {

        Page<HotelReviewResponse> hotelReviewPage = reviewService.getHotelReviewResponses(hotelId, page);
        HotelReviewListResponse hotelReviewListResponse = new HotelReviewListResponse(
                new PageDto<>(hotelReviewPage),
                hotelService.getHotelById(hotelId).getAverageRating());

        return new RsData<>(
                "200-1",
                "호텔 리뷰 목록 생성",
                hotelReviewListResponse
        );
    }
}
