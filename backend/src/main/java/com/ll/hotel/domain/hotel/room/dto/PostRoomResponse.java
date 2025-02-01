package com.ll.hotel.domain.hotel.room.dto;

import com.ll.hotel.domain.hotel.room.entity.Room;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.NonNull;

public record PostRoomResponse(
        long roomId,

        long hotelId,

        @NotBlank
        String roomName,

        @NonNull
        Integer basePrice,

        @NonNull
        Integer standardNumber,

        @NonNull
        Integer maxNumber,

        @NonNull
        LocalDateTime createdAt
) {
    public PostRoomResponse(Room room) {
        this(
                room.getId(),
                room.getHotel().getId(),
                room.getRoomName(),
                room.getBasePrice(),
                room.getStandardNumber(),
                room.getMaxNumber(),
                room.getCreatedAt()
        );
    }
}