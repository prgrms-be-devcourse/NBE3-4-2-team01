package com.ll.hotel.domain.hotel.room.dto;

import com.ll.hotel.domain.hotel.room.entity.Room;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.NonNull;

public record PutRoomResponse(
        long hotelId,

        long roomId,

        @NotBlank
        String roomName,

        @NotBlank
        String roomStatus,

        @NonNull
        LocalDateTime modifiedAt
) {
    public PutRoomResponse(Room room) {
        this(
                room.getHotel().getId(),
                room.getId(),
                room.getRoomName(),
                room.getRoomStatus().getValue(),
                room.getModifiedAt()
        );
    }
}
