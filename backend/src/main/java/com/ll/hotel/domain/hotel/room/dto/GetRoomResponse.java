package com.ll.hotel.domain.hotel.room.dto;

import com.ll.hotel.domain.hotel.option.roomOption.entity.RoomOption;
import com.ll.hotel.domain.hotel.room.entity.Room;
import com.ll.hotel.domain.hotel.room.type.BedTypeNumber;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
        List<RoomImageDto> roomImages,

        @NonNull
        Set<String> roomOptions
) {
    public GetRoomResponse(Room room) {
        this(
                room.getId(),
                room.getHotel().getId(),
                room.getRoomName(),
                room.getRoomNumber(),
                room.getBasePrice(),
                room.getStandardNumber(),
                room.getMaxNumber(),
                room.getBedTypeNumber(),
                room.getRoomStatus().getValue(),
                room.getRoomImages().stream()
                        .map(RoomImageDto::new)
                        .collect(Collectors.toList()),
                room.getRoomOptions() != null
                        ? room.getRoomOptions().stream()
                        .map(RoomOption::getName)
                        .collect(Collectors.toSet())
                        : new HashSet<>()
        );
    }
}
