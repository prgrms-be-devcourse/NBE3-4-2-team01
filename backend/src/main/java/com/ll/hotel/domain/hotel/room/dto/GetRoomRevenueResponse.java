package com.ll.hotel.domain.hotel.room.dto;

import jakarta.validation.constraints.NotBlank;
public record GetRoomRevenueResponse(
        long roomId,

        @NotBlank
        String roomName,

        int basePrice,

        long roomRevenue
) {
}
