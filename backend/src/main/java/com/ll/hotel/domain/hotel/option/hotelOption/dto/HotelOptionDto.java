package com.ll.hotel.domain.hotel.option.hotelOption.dto;

import com.ll.hotel.domain.hotel.option.hotelOption.entity.HotelOption;

public record HotelOptionDto(
        String name
) {
    public static HotelOptionDto toDto(HotelOption hotelOption) {
        return new HotelOptionDto(hotelOption.getName());
    }
}
