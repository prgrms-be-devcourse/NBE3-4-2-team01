package com.ll.hotel.domain.booking.payment.dto;

public record UidsResponse(
        String apiId,
        String channelKey,
        String merchantUid
) {
}
