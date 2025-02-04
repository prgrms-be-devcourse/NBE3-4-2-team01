package com.ll.hotel.domain.review.review.entity;

import com.ll.hotel.domain.booking.booking.entity.Booking;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.room.entity.Room;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.review.review.type.ReviewStatus;
import com.ll.hotel.global.jpa.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "reviews")
@EntityListeners(AuditingEntityListener.class)
public class Review extends BaseTime {

    @ManyToOne(fetch = FetchType.LAZY)
    private Hotel hotel;

    @ManyToOne(fetch = FetchType.LAZY)
    private Room room;

    @OneToOne(fetch = FetchType.LAZY)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(length = 300, nullable = false)
    @Setter
    private String content;

    @Column(nullable = false)
    @Setter
    private Integer rating;

    @Enumerated(EnumType.STRING)
    @Setter
    private ReviewStatus reviewStatus;

    @Builder
    private Review(
            Hotel hotel,
            Booking booking,
            Member member,
            String content,
            Integer rating,
            ReviewStatus reviewStatus) {

        this.hotel = hotel;
        this.booking = booking;
        this.member = member;
        this.content = content;
        this.rating = rating;
        this.reviewStatus = reviewStatus;
    }

    public boolean isWrittenBy(Member member) {
        return this.member.equals(member);
    }
}