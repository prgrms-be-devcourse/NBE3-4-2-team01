package com.ll.hotel.domain.hotel.hotel.dto;

import com.ll.hotel.domain.image.entity.Image;
import jakarta.validation.constraints.NotBlank;

public record HotelImageDto(
        long id,

        @NotBlank
        String hotelImageUrl
) {
    public HotelImageDto(Image image) {
        this(
                image.getId(),
                image.getImageUrl() != null ? image.getImageUrl() : "/images/default.jpg"
        );
    }
}
