package com.ll.hotel.domain.hotel.room.dto;

import java.util.Set;
import lombok.NonNull;

public record GetRoomOptionResponse(
        long roomId,

        @NonNull
        Set<String> roomOptions
) {
}
