package com.ll.hotel.domain.booking.payment.dto;

public record PaymentRequest(
        String merchantUid,
        int amount,
        Long paidAtTimestamp
) {
}

