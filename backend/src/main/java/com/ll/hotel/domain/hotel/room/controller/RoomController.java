package com.ll.hotel.domain.hotel.room.controller;

import com.ll.hotel.domain.hotel.room.dto.PostRoomRequest;
import com.ll.hotel.domain.hotel.room.dto.PostRoomResponse;
import com.ll.hotel.domain.hotel.room.service.RoomService;
import com.ll.hotel.global.rsData.RsData;
import com.ll.hotel.standard.base.Empty;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hotels/{hotelId}/rooms")
@Tag(name = "RoomController", description = "객실 컨트롤러")
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    public RsData<PostRoomResponse> roomCreate(@PathVariable long hotelId,
                                               @RequestBody @Valid PostRoomRequest postRoomRequest) {
        return new RsData<>(
                "201-1",
                "객실을 추가하였습니다.",
                this.roomService.create(hotelId, postRoomRequest)
        );
    }

    @DeleteMapping("/{roomId}")
    public RsData<Empty> deleteRoom(@PathVariable long hotelId, @PathVariable long roomId) {
        this.roomService.delete(hotelId, roomId);

        return RsData.OK;
    }
}
