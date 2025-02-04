package com.ll.hotel.domain.hotel.hotel.entity;

import com.ll.hotel.domain.hotel.hotel.type.HotelStatus;
import com.ll.hotel.domain.hotel.option.hotelOption.entity.HotelOption;
import com.ll.hotel.domain.hotel.room.entity.Room;
import com.ll.hotel.domain.image.entity.Image;
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
    @Column
    private String hotelName;

    @Column(unique = true)
    private String hotelEmail;

    @Column
    private String hotelPhoneNumber;

    @Column
    private String streetAddress;

    @Column
    private Integer zipCode;

    @Column
    private Integer hotelGrade;

    @Column
    private LocalTime checkInTime;

    @Column
    private LocalTime checkOutTime;

    @Column(columnDefinition = "TEXT")
    private String hotelExplainContent;

    @Column
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private HotelStatus hotelStatus = HotelStatus.PENDING;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Room> rooms = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "referenceId")
    @Builder.Default
    private List<Image> hotelImages = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    private Business business;

    @ManyToMany
    Set<HotelOption> hotelOptions;

    @ManyToMany
    Set<Member> favorites;

    @Column(nullable = false)
    private Double averageRating;

    @Column(nullable = false)
    private Long totalReviewRatingSum;

    @Column(nullable = false)
    private Long totalReviewCount;

    // 평균 레이팅 업데이트
    public void updateAverageRating(int countOffset, int ratingOffset) {
        this.totalReviewCount += countOffset;
        this.totalReviewRatingSum += ratingOffset;
        this.averageRating = Math.round(((double)totalReviewRatingSum / totalReviewCount) * 10.0) / 10.0;
    }

    /**
     * 불필요 시 삭제
     */
    @PreRemove
    private void preRemove() {
        if (this.business != null) {
            this.business.setHotel(null);
            this.business.setHotel(null);
        }
    }

    @PrePersist
    public void prePersist() {
        if (averageRating == null) {
            averageRating = 0.0;
        }
        if (totalReviewRatingSum == null) {
            totalReviewRatingSum = 0L;
        }
        if (totalReviewCount == null) {
            totalReviewCount = 0L;
        }
    }

    @PrePersist
    public void prePersist() {
        if (averageRating == null) {
            averageRating = 0.0;
        }
        if (totalReviewRatingSum == null) {
            totalReviewRatingSum = 0L;
        }
        if (totalReviewCount == null) {
            totalReviewCount = 0L;
        }
    }
}
