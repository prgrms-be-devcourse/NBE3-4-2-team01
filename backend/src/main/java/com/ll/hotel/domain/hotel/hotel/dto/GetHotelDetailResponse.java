package com.ll.hotel.domain.hotel.hotel.dto;

import java.util.List;
import java.util.Objects;
import lombok.NonNull;

public record GetHotelDetailResponse(
        @NonNull
        HotelDetailDto hotelDetailDto,

        @NonNull
        List<String> hotelImageUrls
) {
    public GetHotelDetailResponse(HotelDetailDto hotelDetailDto, List<String> hotelImageUrls) {
        this.hotelDetailDto = hotelDetailDto;
        this.hotelImageUrls = Objects.requireNonNullElse(hotelImageUrls, List.of());
    }
}
