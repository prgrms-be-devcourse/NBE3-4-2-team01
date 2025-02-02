package com.ll.hotel.domain.hotel.hotel.dto;

import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

public record GetAllHotelResponse(
        long hotelId,

        @NotBlank
        String hotelName,

        @NotBlank
        String streetAddress,

        @NonNull
        Integer zipCode,

        @NotBlank
        String hotelStatus,

        @NotBlank
        String thumbnailUrl
) {
    public GetAllHotelResponse(Hotel hotel) {
        this(
                hotel.getId(),
                hotel.getHotelName(),
                hotel.getStreetAddress(),
                hotel.getZipCode(),
                hotel.getHotelStatus().getValue(),
                hotel.getHotelImages().isEmpty()
                        ? "/images/default.jpg"
                        : hotel.getHotelImages().getFirst().getImageUrl()
        );
    }
}
