package com.ll.hotel.domain.hotel.room.dto;

import com.ll.hotel.domain.hotel.room.entity.Room;
import com.ll.hotel.domain.review.review.dto.response.PresignedUrlsResponse;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.NonNull;

public record PostRoomResponse(
        long roomId,

        long hotelId,

        @NotBlank
        String roomName,

        int basePrice,

        int standardNumber,

        int maxNumber,

        @NonNull
        LocalDateTime createdAt,

        @NonNull
        PresignedUrlsResponse urlsResponse
) {
    public PostRoomResponse(Room room, PresignedUrlsResponse response) {
        this(
                room.getId(),
                room.getHotel().getId(),
                room.getRoomName(),
                room.getBasePrice(),
                room.getStandardNumber(),
                room.getMaxNumber(),
                room.getCreatedAt(),
                response
        );
    }
}