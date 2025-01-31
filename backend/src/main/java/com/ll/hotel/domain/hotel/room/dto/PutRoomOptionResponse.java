package com.ll.hotel.domain.hotel.room.dto;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.NonNull;

public record PutRoomOptionResponse(
        long roomId,

        @NonNull
        Set<String> roomOptions,

        @NonNull
        LocalDateTime modifiedAt
) {
}
