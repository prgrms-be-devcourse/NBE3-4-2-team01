package com.ll.hotel.domain.booking.payment.dto;

import com.ll.hotel.domain.booking.payment.entity.Payment;
import com.ll.hotel.domain.booking.payment.type.PaymentStatus;

public record PaymentResponse(
        Long paymentId,
        String merchantUid,
        String impUid,
        PaymentStatus paymentStatus
) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getMerchantUid(),
                payment.getImpUid(),
                payment.getPaymentStatus()
        );
    }
}
