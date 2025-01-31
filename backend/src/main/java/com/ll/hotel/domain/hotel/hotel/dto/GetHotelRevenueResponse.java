package com.ll.hotel.domain.hotel.hotel.dto;

import lombok.NonNull;

/**
 * 검토 필요
 */
public record GetHotelRevenueResponse(
        @NonNull
        HotelDto hotelDto,

        @NonNull
        Long revenue
) {
}
