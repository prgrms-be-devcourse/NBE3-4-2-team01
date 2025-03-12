package com.ll.hotel.domain.hotel.room.dto;

import com.ll.hotel.domain.hotel.room.entity.Room;
import com.ll.hotel.domain.image.entity.Image;
import lombok.NonNull;

public record RoomWithImageDto(
        @NonNull
        Room room,

        Image image
) {
}
