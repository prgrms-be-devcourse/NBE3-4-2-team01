package com.ll.hotel.domain.hotel.hotel.dto;

import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
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
    public PostHotelResponse(Hotel hotel) {
        this(
                hotel.getBusiness().getId(),
                hotel.getId(),
                hotel.getHotelName(),
                hotel.getCreatedAt()
        );
    }
}
