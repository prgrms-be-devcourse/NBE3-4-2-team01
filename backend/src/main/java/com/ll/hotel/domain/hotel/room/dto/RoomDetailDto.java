package com.ll.hotel.domain.hotel.room.dto;

import com.ll.hotel.domain.hotel.room.type.BedTypeNumber;
import com.ll.hotel.domain.hotel.room.type.RoomStatus;
import com.ll.hotel.domain.image.Image;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

/**
 * 현재 findRoomDetail이 잘 작동하면 삭제
 */
public record RoomDetailDto(
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
        Image roomImage
) {
    public RoomDetailDto(long id, long hotelId, String roomName, Integer roomNumber, Integer basePrice,
                         Integer standardNumber, Integer maxNumber, BedTypeNumber bedTypeNumber,
                         RoomStatus roomStatus,
                         Image image) {
        this(
                id,
                hotelId,
                roomName,
                roomNumber,
                basePrice,
                standardNumber,
                maxNumber,
                bedTypeNumber,
                roomStatus.getValue(),
                image
        );
    }
}
