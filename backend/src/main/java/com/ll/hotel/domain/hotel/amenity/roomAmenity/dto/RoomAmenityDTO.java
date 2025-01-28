package com.ll.hotel.domain.hotel.amenity.roomAmenity.dto;

import com.ll.hotel.domain.hotel.amenity.roomAmenity.entity.RoomAmenity;
import lombok.Builder;

@Builder
public record RoomAmenityDTO (
        String description
) {
    public static RoomAmenityDTO toDTO(RoomAmenity roomAmenity) {

        return RoomAmenityDTO.builder()
                .description(roomAmenity.getDescription())
                .build();
    }
}

