package com.ll.hotel.domain.review.review.service;

import com.ll.hotel.domain.booking.booking.entity.Booking;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.room.entity.Room;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.review.review.entity.Review;
import com.ll.hotel.domain.review.review.repository.ReviewRepository;
import com.ll.hotel.domain.review.review.type.ReviewStatus;
import com.ll.hotel.global.exceptions.ServiceException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final EntityManager entityManager;
    private final ReviewRepository reviewRepository;

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
}
