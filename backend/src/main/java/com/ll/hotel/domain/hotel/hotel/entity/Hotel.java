package com.ll.hotel.domain.hotel.hotel.entity;

import com.ll.hotel.domain.hotel.hotel.dto.PostHotelRequest;
import com.ll.hotel.domain.hotel.hotel.type.HotelStatus;
import com.ll.hotel.domain.hotel.option.entity.HotelOption;
import com.ll.hotel.domain.hotel.room.entity.Room;
import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.global.jpa.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Hotel extends BaseTime {
    @Column(nullable = false)
    private String hotelName;

    @Column(unique = true, nullable = false)
    private String hotelEmail;

    @Column(nullable = false)
    private String hotelPhoneNumber;

    @Column(nullable = false)
    private String streetAddress;

    @Column(nullable = false)
    private Integer zipCode;

    @Column(nullable = false)
    private Integer hotelGrade;

    @Column(nullable = false)
    private LocalTime checkInTime;

    @Column(nullable = false)
    private LocalTime checkOutTime;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String hotelExplainContent;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private HotelStatus hotelStatus = HotelStatus.PENDING;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Room> rooms = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    private Business business;

    @ManyToMany
    Set<HotelOption> hotelOptions;

    @ManyToMany
    Set<Member> favorites;

    @Column(nullable = false)
    @Builder.Default
    private Double averageRating = 0.0;

    @Column(nullable = false)
    @Builder.Default
    private Long totalReviewRatingSum = 0L;

    @Column(nullable = false)
    @Builder.Default
    private Long totalReviewCount = 0L;

    // 평균 레이팅 업데이트
    public void updateAverageRating(int countOffset, int ratingOffset) {
        this.totalReviewCount += countOffset;
        this.totalReviewRatingSum += ratingOffset;
        this.averageRating = Math.round(((double) totalReviewRatingSum / totalReviewCount) * 10.0) / 10.0;
    }

    // 호텔 소유자 확인
    public boolean isOwnedBy(Member member) {
        return this.business != null && this.business.getMember().equals(member);
    }

    public static Hotel from(PostHotelRequest request, Business business, Set<HotelOption> hotelOptions) {
        return Hotel.builder()
                .hotelName(request.hotelName())
                .hotelEmail(request.hotelEmail())
                .hotelPhoneNumber(request.hotelPhoneNumber())
                .streetAddress(request.streetAddress())
                .zipCode(request.zipCode())
                .hotelGrade(request.hotelGrade())
                .checkInTime(request.checkInTime())
                .checkOutTime(request.checkOutTime())
                .hotelExplainContent(request.hotelExplainContent())
                .business(business)
                .hotelOptions(hotelOptions)
                .build();
    }
}
