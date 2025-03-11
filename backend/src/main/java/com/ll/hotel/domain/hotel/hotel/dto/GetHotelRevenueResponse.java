package com.ll.hotel.domain.hotel.hotel.dto;

import com.ll.hotel.domain.hotel.room.dto.GetRoomRevenueResponse;
import java.util.List;
import lombok.NonNull;

public record GetHotelRevenueResponse(
        @NonNull
        List<GetRoomRevenueResponse> roomRevenueResponse,

        @NonNull
        Long revenue
) {
}
