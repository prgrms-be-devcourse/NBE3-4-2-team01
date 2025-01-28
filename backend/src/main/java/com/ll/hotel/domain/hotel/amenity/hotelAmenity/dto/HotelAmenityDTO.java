package com.ll.hotel.domain.hotel.amenity.hotelAmenity.dto;

import com.ll.hotel.domain.hotel.amenity.hotelAmenity.entity.HotelAmenity;
import lombok.Builder;

@Builder
public record HotelAmenityDTO (
    String description
) {
    public static HotelAmenityDTO toDTO(HotelAmenity hotelAmenity) {

        return HotelAmenityDTO.builder()
                .description(hotelAmenity.getDescription())
                .build();
    }
}
