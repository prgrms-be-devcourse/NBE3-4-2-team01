package com.ll.hotel.domain.hotel.hotel.dto;

import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
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
    public PutHotelResponse(Hotel hotel) {
        this(
                hotel.getBusiness().getId(),
                hotel.getId(),
                hotel.getHotelName(),
                hotel.getHotelStatus().getValue(),
                hotel.getModifiedAt()
        );
    }
}
