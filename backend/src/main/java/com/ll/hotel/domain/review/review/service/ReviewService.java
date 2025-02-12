package com.ll.hotel.domain.review.review.service;

import com.ll.hotel.domain.booking.booking.entity.Booking;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.repository.HotelRepository;
import com.ll.hotel.domain.hotel.room.entity.Room;
import com.ll.hotel.domain.image.dto.ImageDto;
import com.ll.hotel.domain.image.entity.Image;
import com.ll.hotel.domain.image.repository.ImageRepository;
import com.ll.hotel.domain.image.type.ImageType;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.review.review.dto.ReviewDto;
import com.ll.hotel.domain.review.review.dto.response.*;
import com.ll.hotel.domain.review.review.entity.Review;
import com.ll.hotel.domain.review.review.repository.ReviewRepository;
import com.ll.hotel.global.exceptions.ServiceException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final EntityManager entityManager;
    private final ReviewRepository reviewRepository;
    private final ImageRepository imageRepository;
    private final HotelRepository hotelRepository;

    // 리뷰 생성
    public long createReview(Long hotelId, Long roomId, Long memberId, Long bookingId, String content, int rating) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ServiceException("400-1", "존재하지 않는 호텔입니다."));
        Member member = entityManager.getReference(Member.class, memberId);
        Room room = entityManager.getReference(Room.class, roomId);
        Booking booking = entityManager.getReference(Booking.class, bookingId);

        if(!booking.isReservedBy(member)) {
            throw new ServiceException("403-1", "예약자만 리뷰 생성이 가능합니다.");
        }

        // 호텔 평균 리뷰 수정
        updateRatingOnReviewCreated(hotel, rating);

        Review review = Review.builder()
                .hotel(hotel)
                .room(room)
                .member(member)
                .booking(booking)
                .content(content)
                .rating(rating)
                .build();

        Review savedReview = reviewRepository.save(review);
        return savedReview.getId();
    }

    // 리뷰의 content, rating 수정
    public void updateReviewContentAndRating(Member actor, long reviewId, String content, int rating){
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ServiceException("400-1", "수정할 리뷰가 존재하지 않습니다."));

        if(!review.isWrittenBy(actor)) {
            throw new ServiceException("403-1", "리뷰 작성자만 리뷰 수정 가능합니다.");
        }

        // 호텔 평균 리뷰 수정
        updateRatingOnReviewModified(review.getHotel(), review.getRating(), rating);

        review.setContent(content);
        review.setRating(rating);
    }

    // 리뷰 삭제
    public void deleteReview(Member actor, long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ServiceException("400-1", "삭제할 리뷰가 존재하지 않습니다."));

        if(!review.isWrittenBy(actor)) {
            throw new ServiceException("403-1", "리뷰 작성자만 리뷰 삭제 가능합니다.");
        }

        // 호텔 평균 리뷰 수정
        updateRatingOnReviewDeleted(review.getHotel(), review.getRating());

        reviewRepository.delete(review);
    }

    // 리뷰 단건 조회
    public GetReviewResponse getReviewResponse(Member actor, long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ServiceException("400-1", "해당 리뷰가 존재하지 않습니다."));

        if(!review.isWrittenBy(actor)) {
            throw new ServiceException("403-1", "리뷰 작성자만 리뷰 수정 가능합니다.");
        }

        List<String> imageUrls = imageRepository.findByImageTypeAndReferenceId(ImageType.REVIEW, reviewId)
                .stream()
                .map(Image::getImageUrl)
                .toList();

        return new GetReviewResponse(new ReviewDto(review), imageUrls);
    }

    // 현재 접속한 유저가 작성한 모든 리뷰 조회 (답변, 이미지 포함)
    public Page<MyReviewResponse> getMyReviewResponses(Member actor, int page) {

        if (!actor.isUser()) {
            throw new ServiceException("403-1", "관리자, 사업자는 리뷰 목록 조회가 불가능합니다.");
        }

        int size = 10;
        Pageable pageable = PageRequest.of(page-1, size, Sort.by("createdAt").descending());
        Page<MyReviewWithCommentDto> myReviews = reviewRepository.findReviewsWithCommentByMemberId(actor.getId(), pageable);

        return getReviewsWithImages(
                myReviews,
                myReview -> myReview.reviewDto().reviewId(),
                MyReviewResponse::new,
                pageable
        );
    }

    // 호텔의 모든 리뷰 조회 (답변, 이미지 포함)
    public Page<HotelReviewResponse> getHotelReviewResponses(long hotelId, int page) {
        int size = 10;
        Pageable pageable = PageRequest.of(page-1, size, Sort.by("createdAt").descending());
        Page<HotelReviewWithCommentDto> hotelReviews = reviewRepository.findReviewsWithCommentByHotelId(hotelId, pageable);

        return getReviewsWithImages(
                hotelReviews,
                hotelReview -> hotelReview.reviewDto().reviewId(),
                HotelReviewResponse::new,
                pageable
        );
    }

    public Review getReview(long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ServiceException("400-1", "존재하지 않는 리뷰입니다"));
    }

    // 리뷰 생성의 평균 리뷰 수정
    private void updateRatingOnReviewCreated(Hotel hotel, int rating) {
        hotel.updateAverageRating(1, rating);
    }

    // 리뷰 수정의 평균 리뷰 수정
    private void updateRatingOnReviewModified(Hotel hotel, int beforeRating, int afterRating) {
        hotel.updateAverageRating(0, afterRating - beforeRating);
    }

    // 리뷰 삭제의 평균 리뷰 수정
    private void updateRatingOnReviewDeleted(Hotel hotel, int rating) {
        hotel.updateAverageRating(-1, -rating);
    }

    // 리뷰 목록을 받아 (리뷰 + 사진) 목록으로 반환
    private <T, R> Page<R> getReviewsWithImages(Page<T> reviews, Function<T, Long> getReviewId, BiFunction<T, List<String>, R> mapToResponse, Pageable pageable) {
        // 리뷰 아이디 추출
        List<Long> reviewIds = reviews.getContent().stream()
                .map(getReviewId)
                .toList();

        // 이미지 URL 매핑
        Map<Long, List<String>> reviewImageUrls = imageRepository.findImageUrlsByReferenceIdsAndImageType(reviewIds, ImageType.REVIEW, pageable)
                .getContent()
                .stream()
                .collect(Collectors.groupingBy(
                        ImageDto::referenceId,
                        Collectors.mapping(ImageDto::imageUrl, Collectors.toList())));

        // 응답 객체 생성
        List<R> responseList = reviews.getContent().stream()
                .map(review -> mapToResponse.apply(review, reviewImageUrls.getOrDefault(getReviewId.apply(review), List.of())))
                .toList();

        return new PageImpl<>(responseList, pageable, reviews.getTotalElements());
    }
}
