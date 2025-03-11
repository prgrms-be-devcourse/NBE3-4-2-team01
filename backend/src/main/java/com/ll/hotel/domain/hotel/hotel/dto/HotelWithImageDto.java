package com.ll.hotel.domain.hotel.hotel.dto;

import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.image.entity.Image;
import lombok.NonNull;

public record HotelWithImageDto(
        @NonNull
        Hotel hotel,

        Image image
) {
}
