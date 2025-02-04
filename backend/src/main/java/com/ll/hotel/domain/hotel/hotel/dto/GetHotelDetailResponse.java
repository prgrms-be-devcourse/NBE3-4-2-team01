package com.ll.hotel.domain.hotel.hotel.dto;

import com.ll.hotel.standard.util.Ut;
import java.util.List;

public record GetHotelDetailResponse(
        HotelDto hotelDto,

        List<String> hotelImageUrls
) {
    public GetHotelDetailResponse(HotelDto hotelDto, List<String> hotelImageUrls) {
        this.hotelDto = hotelDto;
        this.hotelImageUrls = Ut.list.hasValue(hotelImageUrls)
                ? hotelImageUrls
                : List.of();
    }
}
