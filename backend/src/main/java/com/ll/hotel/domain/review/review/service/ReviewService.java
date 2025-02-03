package com.ll.hotel.domain.review.review.service;

import com.ll.hotel.domain.booking.booking.entity.Booking;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.room.entity.Room;
import com.ll.hotel.domain.image.entity.Image;
import com.ll.hotel.domain.image.dto.ImageDto;
import com.ll.hotel.domain.image.repository.ImageRepository;
import com.ll.hotel.domain.image.type.ImageType;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.review.review.dto.*;
import com.ll.hotel.domain.review.review.entity.Review;
import com.ll.hotel.domain.review.review.repository.ReviewRepository;
import com.ll.hotel.domain.review.review.type.ReviewStatus;
import com.ll.hotel.global.exceptions.ServiceException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final EntityManager entityManager;
    private final ReviewRepository reviewRepository;
    private final ImageRepository imageRepository;

    // 리뷰 생성
    public long createReview(Long hotelId, Long roomId, Long memberId, Long bookingId, String content, int rating) {
        Hotel hotel = entityManager.getReference(Hotel.class, hotelId);
        Member member = entityManager.getReference(Member.class, memberId);
        Room room = entityManager.getReference(Room.class, roomId);
        Booking booking = entityManager.getReference(Booking.class, bookingId);

        Review review = Review.builder()
                .hotel(hotel)
                .room(room)
                .member(member)
                .booking(booking)
                .content(content)
                .rating(rating)
                .reviewStatus(ReviewStatus.CREATED)
                .build();

        Review savedReview = reviewRepository.save(review);
        return savedReview.getId();
    }

    // 리뷰의 content, rating 수정
    public void updateReviewContentAndRating(long reviewId, String content, int rating){
        Review review = reviewRepository.findByIdWithFilter(reviewId)
                .orElseThrow(() -> new ServiceException("400-1", "수정할 리뷰가 존재하지 않습니다."));

        review.setContent(content);
        review.setRating(rating);
        review.setReviewStatus(ReviewStatus.UPDATED);
    }

    // 리뷰 삭제
    public void deleteReview(long reviewId) {
        Review review = reviewRepository.findByIdWithFilter(reviewId)
                .orElseThrow(() -> new ServiceException("400-1", "삭제할 리뷰가 존재하지 않습니다."));

        review.setReviewStatus(ReviewStatus.DELETED);
    }

    // 리뷰 단건 조회
    public GetReviewResponse getReviewResponse(long reviewId) {
        Review review = reviewRepository.findByIdWithFilter(reviewId)
                .orElseThrow(() -> new ServiceException("400-1", "해당 리뷰가 존재하지 않습니다."));

        ReviewDto reviewDto = new ReviewDto(review);

        List<String> imageUrls = imageRepository.findByImageTypeAndReferenceId(ImageType.REVIEW, reviewId)
                .stream()
                .map(Image::getImageUrl)
                .toList();

        return new GetReviewResponse(reviewDto, imageUrls);
    }

    // 현재 접속한 유저가 작성한 모든 리뷰 조회 (답변, 이미지 포함)
    public List<MyReviewResponse> getMyReviewResponses(long memberId) {
        List<MyReviewWithCommentDto> myReviews = reviewRepository.findReviewsWithCommentByMemberId(memberId);

        List<Long> reviewIds = myReviews.stream()
                .map(dto -> dto.reviewDto().reviewId())
                .toList();

        Map<Long, List<String>> reviewImageUrls = imageRepository.findImageUrlsByReviewIdsAndImageType(
                        reviewIds, ImageType.REVIEW)
                .stream()
                .collect(Collectors.groupingBy(
                        ImageDto::referenceId, // referenceId로 그룹화
                        Collectors.mapping(ImageDto::imageUrl, Collectors.toList())));

        return myReviews.stream()
                .map(myReview -> new MyReviewResponse(
                        myReview,
                        reviewImageUrls.getOrDefault(myReview.reviewDto().reviewId(), List.of()) // 이미지 URL 매핑
                ))
                .toList();
    }

    // 호텔의 모든 리뷰 조회 (답변, 이미지 포함)
    public List<HotelReviewResponse> getHotelReviewResponses(long hotelId) {
        List<HotelReviewWithCommentDto> hotelReviews = reviewRepository.findReviewsWithCommentByHotelId(hotelId);

        List<Long> reviewIds = hotelReviews.stream()
                .map(dto -> dto.reviewDto().reviewId())
                .toList();

        Map<Long, List<String>> reviewImageUrls = imageRepository.findImageUrlsByReviewIdsAndImageType(
                        reviewIds, ImageType.REVIEW)
                .stream()
                .collect(Collectors.groupingBy(
                        ImageDto::referenceId, // referenceId로 그룹화
                        Collectors.mapping(ImageDto::imageUrl, Collectors.toList())));

        return hotelReviews.stream()
                .map(hotelReview -> new HotelReviewResponse(
                        hotelReview,
                        reviewImageUrls.getOrDefault(hotelReview.reviewDto().reviewId(), List.of()) // 이미지 URL 매핑
                ))
                .toList();
    }
}
