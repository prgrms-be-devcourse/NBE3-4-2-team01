package com.ll.hotel.domain.hotel.hotel.dto;

import com.ll.hotel.domain.hotel.option.hotelOption.entity.HotelOption;
import jakarta.validation.constraints.NotBlank;

public record HotelOptionDto(
        long id,

        @NotBlank
        String hotelOptionName
) {
    public HotelOptionDto(HotelOption hotelOption) {
        this(
                hotelOption.getId(),
                hotelOption.getName()
        );
    }
}
