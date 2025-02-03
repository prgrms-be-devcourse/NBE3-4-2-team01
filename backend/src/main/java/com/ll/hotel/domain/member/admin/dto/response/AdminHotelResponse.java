package com.ll.hotel.domain.member.admin.dto.response;

import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.type.HotelStatus;

public record AdminHotelResponse(
        String hotelName,
        HotelStatus hotelStatus
) {
    public AdminHotelResponse(Hotel hotel) {
        this(hotel.getHotelName(), hotel.getHotelStatus());
    }
}
