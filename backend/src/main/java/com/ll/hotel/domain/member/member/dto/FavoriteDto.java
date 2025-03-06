package com.ll.hotel.domain.hotel.hotel.dto;

import com.ll.hotel.domain.hotel.hotel.entity.Hotel;

import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

public record FavoriteHotelDto(
        long hotelId,

        @NotBlank
        String hotelName,

        @NotBlank
        String streetAddress,

        @NonNull
        Integer hotelGrade,

        @NotBlank
        String hotelStatus
) {
    public static FavoriteHotelDto from(Hotel hotel) {
        return new FavoriteHotelDto(
                hotel.getId(),
                hotel.getHotelName(),
                hotel.getStreetAddress(),
                hotel.getHotelGrade(),
                hotel.getHotelStatus().getValue()
        );
    }
} 