package com.ll.hotel.domain.review.review.service;

import com.ll.hotel.domain.booking.booking.entity.Booking;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.room.entity.Room;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.review.review.entity.Review;
import com.ll.hotel.domain.review.review.repository.ReviewRepository;
import com.ll.hotel.domain.review.review.type.ReviewStatus;
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
}
