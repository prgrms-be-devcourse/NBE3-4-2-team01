package com.ll.hotel.domain.hotel.room.controller;

import com.ll.hotel.domain.hotel.room.dto.PostRoomRequest;
import com.ll.hotel.domain.hotel.room.dto.PostRoomResponse;
import com.ll.hotel.domain.hotel.room.service.RoomService;
import com.ll.hotel.global.rsData.RsData;
import com.ll.hotel.standard.base.Empty;
import com.ll.hotel.domain.hotel.room.dto.GetRoomResponse;
import com.ll.hotel.domain.hotel.room.dto.PutRoomRequest;
import com.ll.hotel.domain.hotel.room.dto.PutRoomResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import com.ll.hotel.domain.hotel.room.dto.GetAllRoomResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import com.ll.hotel.domain.hotel.room.dto.GetRoomOptionResponse;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping
    public RsData<List<GetAllRoomResponse>> findAllRooms(@PathVariable long hotelId) {
        return new RsData<>(
                "200",
                "모든 객실 정보를 정상적으로 불러왔습니다.",
                this.roomService.findAllRooms(hotelId)
        );
    }

    @GetMapping("/{roomId}")
    public RsData<GetRoomResponse> findRoomDetail(@PathVariable long hotelId, @PathVariable long roomId) {
        return new RsData<>(
                "200",
                "객실 정보를 정상적으로 불러왔습니다.",
                this.roomService.findRoomDetail(hotelId, roomId)
        );
    }

    @GetMapping("/{roomId}/room-option")
    public RsData<GetRoomOptionResponse> findRoomOptions(@PathVariable long hotelId, @PathVariable long roomId) {
        return new RsData<>(
                "200",
                "객실의 옵션 정보를 정상적으로 불러왔습니다.",
                this.roomService.findRoomOptions(hotelId, roomId)
        );
    }

    @PutMapping("{roomId}")
    public RsData<PutRoomResponse> modify(@PathVariable long hotelId,
                                          @PathVariable long roomId,
                                          @RequestBody PutRoomRequest request) {
        return new RsData<>(
                "200-1",
                "객실 정보를 수정했습니다.",
                this.roomService.modify(hotelId, roomId, request));
    }
}
