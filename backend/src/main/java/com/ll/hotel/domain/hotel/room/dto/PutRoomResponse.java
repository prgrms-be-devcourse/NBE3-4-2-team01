package com.ll.hotel.domain.hotel.room.dto;

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
}
