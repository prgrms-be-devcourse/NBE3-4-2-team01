package com.ll.hotel.domain.hotel.hotel.dto;

import lombok.NonNull;

public record GetHotelResponse(
        @NonNull
        HotelDto hotelDto
) {
}
