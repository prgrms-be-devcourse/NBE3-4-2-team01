package com.ll.hotel.domain.booking.payment.dto;

import com.ll.hotel.domain.booking.booking.dto.BookingRequest;

public record PaymentRequest(
        String merchantUid,
        int amount,
        Long paidAtTimestamp
) {
    public static PaymentRequest from(BookingRequest bookingRequest) {
        return new PaymentRequest(
                bookingRequest.merchantUid(),
                bookingRequest.amount(),
                bookingRequest.paidAtTimestamp()
        );
    }
}

