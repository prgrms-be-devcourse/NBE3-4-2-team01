package com.ll.hotel.domain.hotel.room.dto;

import com.ll.hotel.domain.hotel.option.roomOption.entity.RoomOption;
import com.ll.hotel.domain.hotel.room.entity.Room;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NonNull;

public record GetRoomOptionResponse(
        long roomId,

        @NonNull
        Set<String> roomOptions
) {
    public GetRoomOptionResponse(Room room) {
        this(
                room.getId(),
                room.getRoomOptions().stream()
                        .map(RoomOption::getName)
                        .collect(Collectors.toSet())
        );
    }
}
