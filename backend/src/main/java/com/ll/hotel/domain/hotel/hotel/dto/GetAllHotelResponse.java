package com.ll.hotel.domain.hotel.hotel.dto;

import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.type.HotelStatus;
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
    public GetAllHotelResponse(long hotelId, String hotelName, String streetAddress,
                               Integer zipCode, HotelStatus hotelStatus, String thumbnailUrl) {
        this(
                hotelId,
                hotelName,
                streetAddress,
                zipCode,
                hotelStatus.getValue(),
                // 썸네일이 없을 시, 기본 썸네일 출력
                thumbnailUrl != null ? thumbnailUrl : "/images/default.jpg"
        );
    }
}
