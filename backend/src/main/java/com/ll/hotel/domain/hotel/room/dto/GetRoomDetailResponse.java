package com.ll.hotel.domain.hotel.room.dto;

import java.util.List;
import java.util.Objects;
import lombok.NonNull;

public record GetRoomDetailResponse(
        @NonNull
        RoomDto roomDto,

        @NonNull
        List<String> roomImageUrls
) {
    public GetRoomDetailResponse(RoomDto roomDto, List<String> roomImageUrls) {
        this.roomDto = roomDto;
        this.roomImageUrls = Objects.requireNonNullElse(roomImageUrls, List.of());
    }
}
