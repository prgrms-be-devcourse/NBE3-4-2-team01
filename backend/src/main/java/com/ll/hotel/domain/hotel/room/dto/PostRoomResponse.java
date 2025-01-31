package com.ll.hotel.domain.hotel.room.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
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
        LocalDate createdAt
) {
}