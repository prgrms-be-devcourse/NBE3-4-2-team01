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

    public GetAllRoomResponse(long roomId, String roomName, Integer basePrice, Integer standardNumber,
                              Integer maxNumber, BedTypeNumber bedTypeNumber, String thumbnailUrl) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.basePrice = basePrice;
        this.standardNumber = standardNumber;
        this.maxNumber = maxNumber;
        this.bedTypeNumber = bedTypeNumber;
        this.thumbnailUrl = thumbnailUrl != null ? thumbnailUrl : "/images/default.jpg";
    }
}
