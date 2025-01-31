package com.ll.hotel.domain.hotel.hotel.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.NonNull;

public record PostHotelResponse(
        long businessId,

        long hotelId,

        @NotBlank
        String hotelName,

        @NonNull
        LocalDateTime createdAt
) {
}
