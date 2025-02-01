package com.ll.hotel.domain.hotel.room.dto;

import com.ll.hotel.domain.hotel.option.roomOption.dto.RoomOptionDto;
import com.ll.hotel.domain.hotel.room.entity.Room;
import com.ll.hotel.domain.hotel.room.type.BedTypeNumber;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NonNull;

public record RoomDto(
        long roomId,

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

        @NonNull
        BedTypeNumber bedTypeNumber,

        @NonNull
        String roomStatus,

//        @NonNull
//        List<BookingDto> bookings,

        @NonNull
        List<RoomImageDto> roomImages,

        @NonNull
        Set<String> roomOptions
) {
    public RoomDto(Room room) {
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
                room.getRoomOptions().stream()
                        .map(RoomOptionDto::toDto)
                        .map(RoomOptionDto::name)
                        .collect(Collectors.toSet())
        );
    }
}
