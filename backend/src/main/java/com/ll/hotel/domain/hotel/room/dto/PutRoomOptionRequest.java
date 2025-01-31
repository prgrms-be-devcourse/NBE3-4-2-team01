package com.ll.hotel.domain.hotel.room.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record PutRoomOptionRequest(
        /**
         * 해당 부분 검토 필요
         */
        @NotNull
        Set<String> options
) {
}
