package com.ll.hotel.domain.booking.booking.entity;

import com.ll.hotel.domain.booking.booking.type.BookingStatus;
import com.ll.hotel.domain.booking.payment.entity.Payment;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.room.entity.Room;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.global.jpa.entity.BaseTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking extends BaseTime {
    @NotNull(message = "객실 정보는 필수입니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @NotNull(message = "호텔 정보는 필수입니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @NotNull(message = "사용자 정보는 필수입니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name =  "member_id")
    private Member member;

    @NotNull(message = "결제 정보는 필수입니다.")
    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @NotNull(message = "예약 번호는 필수입니다.")
    @Column
    @Builder.Default
    private String bookingNumber = "";

    @NotNull(message = "예약 상태 정보는 필수입니다.")
    @Enumerated(EnumType.STRING)
    @Column
    @Builder.Default
    private BookingStatus bookingStatus = BookingStatus.CONFIRMED;

    @NotNull(message = "체크인 일자는 필수입니다.")
    @Column
    private LocalDate checkInDate;

    @NotNull(message = "체크아웃 일자는 필수입니다.")
    @Column
    private LocalDate checkOutDate;

    public boolean isReservedBy(Member member) {
        return this.member.equals(member);
    }

    public boolean isOwnedBy(Member member) {
        return this.member.isBusiness() && member.getBusiness().equals(hotel.getBusiness());
    }
}