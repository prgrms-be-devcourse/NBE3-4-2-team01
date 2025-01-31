package com.ll.hotel.domain.hotel.room.dto;

import com.ll.hotel.domain.hotel.option.roomOption.entity.RoomOption;
import jakarta.validation.constraints.NotBlank;

public record RoomOptionDto(
        long id,

        @NotBlank
        String roomOptionName
) {

    public RoomOptionDto(RoomOption roomOption) {
        this(
                roomOption.getId(),
                roomOption.getName()
        );
    }
}
