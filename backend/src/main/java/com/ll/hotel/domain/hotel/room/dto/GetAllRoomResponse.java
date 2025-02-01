package com.ll.hotel.domain.hotel.room.dto;

import com.ll.hotel.domain.hotel.room.entity.Room;
import com.ll.hotel.domain.hotel.room.type.BedTypeNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

public record GetAllRoomResponse(
        long roomId,

        @NotBlank
        String roomName,

        @NonNull
        Integer basePrice,

        @NonNull
        Integer standardNumber,

        @NonNull
        Integer maxNumber,

        @NotBlank
        BedTypeNumber bedTypeNumber,

        @NonNull
        String thumbnailUrl
) {
    public GetAllRoomResponse(Room room) {
        this(
                room.getId(),
                room.getRoomName(),
                room.getBasePrice(),
                room.getStandardNumber(),
                room.getMaxNumber(),
                room.getBedTypeNumber(),
                room.getRoomImages().isEmpty() ? null
                        : room.getRoomImages().get(0).getImageUrl()
        );
    }
}
