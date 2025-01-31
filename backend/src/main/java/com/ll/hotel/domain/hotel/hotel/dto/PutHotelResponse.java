package com.ll.hotel.domain.hotel.hotel.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.NonNull;

public record PutHotelResponse(
        long businessId,

        long hotelId,

        @NotBlank
        String hotelName,

        @NotBlank
        String hotelStatus,

        @NonNull
        LocalDateTime modifiedAt
) {
}
