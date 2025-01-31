package com.ll.hotel.domain.hotel.room.dto;

import com.ll.hotel.domain.image.Image;
import jakarta.validation.constraints.NotBlank;

public record RoomImageDto(
        long id,

        @NotBlank
        String roomImageUrl
) {
    public RoomImageDto(Image image) {
        this(
                image.getId(),
                image.getImageUrl()
        );
    }
}
