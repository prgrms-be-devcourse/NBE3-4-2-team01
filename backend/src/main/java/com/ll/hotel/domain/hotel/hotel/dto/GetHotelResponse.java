package com.ll.hotel.domain.hotel.hotel.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

public record GetHotelResponse(
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
    public GetHotelResponse(HotelWithImageDto hotelWithImageDto) {
        this(
                hotelWithImageDto.hotel().getId(),
                hotelWithImageDto.hotel().getHotelName(),
                hotelWithImageDto.hotel().getStreetAddress(),
                hotelWithImageDto.hotel().getZipCode(),
                hotelWithImageDto.hotel().getHotelStatus().getValue(),
                hotelWithImageDto.image() == null
                        ? "/images/default.jpg"
                        : hotelWithImageDto.image().getImageUrl()
        );
    }
}
