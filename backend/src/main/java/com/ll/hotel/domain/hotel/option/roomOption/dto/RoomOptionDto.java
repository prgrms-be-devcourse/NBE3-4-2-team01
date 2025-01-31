package com.ll.hotel.domain.hotel.option.roomOption.dto;

import com.ll.hotel.domain.hotel.option.roomOption.entity.RoomOption;

public record RoomOptionDto(
        String name
) {
    public static RoomOptionDto toDto(RoomOption roomOption) {

        return new RoomOptionDto(roomOption.getName());
    }
}
