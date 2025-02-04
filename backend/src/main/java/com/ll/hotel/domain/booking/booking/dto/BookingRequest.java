package com.ll.hotel.domain.booking.booking.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BookingRequest(
        @NotNull(message = "객실 정보는 필수입니다.") Long roomId,
        @NotNull(message = "호텔 정보는 필수입니다.") Long hotelId,
        @NotNull(message = "결제 정보는 필수입니다.") Long paymentId,
        @NotNull(message = "체크인 일자는 필수입니다.") LocalDate checkInDate,
        @NotNull(message = "체크아웃 일자는 필수입니다.") LocalDate checkOutDate,

        // PaymentRequest 생성에 필요
        String merchantUid,
        int amount,
        Long paidAtTimestamp
) {
}
