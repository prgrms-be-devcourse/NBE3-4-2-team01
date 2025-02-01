package com.ll.hotel.domain.hotel.room.dto;

import com.ll.hotel.domain.hotel.room.type.BedTypeNumber;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.NonNull;

public record GetRoomResponse(
        long id,

        long hotelId,

        @NotBlank
        String roomName,

        @NonNull
        Integer roomNumber,

        @NonNull
        Integer basePrice,

        @NonNull
        Integer standardNumber,

        @NonNull
        Integer maxNumber,

        @NotBlank
        BedTypeNumber bedTypeNumber,

        @NotBlank
        String roomStatus,

        @NonNull
        List<RoomImageDto> roomImages
) {
    public GetRoomResponse(RoomDto roomDto) {
        this(
                roomDto.roomId(),
                roomDto.hotelId(),
                roomDto.roomName(),
                roomDto.roomNumber(),
                roomDto.basePrice(),
                roomDto.standardNumber(),
                roomDto.maxNumber(),
                roomDto.bedTypeNumber(),
                roomDto.roomStatus(),
                roomDto.roomImages()
        );
    }
}
