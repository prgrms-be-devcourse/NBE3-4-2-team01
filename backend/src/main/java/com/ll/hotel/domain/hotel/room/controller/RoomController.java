package com.ll.hotel.domain.hotel.room.controller;

import com.ll.hotel.domain.hotel.room.dto.GetAllRoomOptionsResponse;
import com.ll.hotel.domain.hotel.room.dto.GetRoomDetailResponse;
import com.ll.hotel.domain.hotel.room.dto.GetRoomOptionResponse;
import com.ll.hotel.domain.hotel.room.dto.GetRoomResponse;
import com.ll.hotel.domain.hotel.room.dto.PostRoomRequest;
import com.ll.hotel.domain.hotel.room.dto.PostRoomResponse;
import com.ll.hotel.domain.hotel.room.dto.PutRoomRequest;
import com.ll.hotel.domain.hotel.room.dto.PutRoomResponse;
import com.ll.hotel.domain.hotel.room.service.RoomService;
import com.ll.hotel.domain.image.type.ImageType;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.global.rq.Rq;
import com.ll.hotel.global.rsData.RsData;
import com.ll.hotel.standard.base.Empty;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hotels/{hotelId}/rooms")
@Tag(name = "RoomController", description = "객실 컨트롤러")
public class RoomController {
    private final RoomService roomService;
    private final Rq rq;

    @PostMapping
    public RsData<PostRoomResponse> createRoom(@PathVariable long hotelId,
                                               @RequestBody @Valid PostRoomRequest postRoomRequest) {
        Member actor = this.rq.getActor();

        return new RsData<>(
                "201-1",
                "객실을 정상적으로 등록하였습니다.",
                this.roomService.createRoom(hotelId, actor, postRoomRequest)
        );
    }

    @PostMapping("/{roomId}/urls")
    public RsData<Empty> saveImageUrls(@PathVariable long hotelId, @PathVariable long roomId,
                                       @RequestBody List<String> urls
    ) {
        Member actor = this.rq.getActor();

        this.roomService.saveImages(actor, ImageType.ROOM, roomId, urls);

        return new RsData<>(
                "201-1",
                "객실 이미지 저장에 성공하였습니다."
        );
    }

    @DeleteMapping("/{roomId}")
    public RsData<Empty> deleteRoom(@PathVariable long hotelId, @PathVariable long roomId) {
        Member actor = this.rq.getActor();

        this.roomService.deleteRoom(hotelId, roomId, actor);

        return new RsData<>(
                "200-1",
                "객실 삭제에 성공하였습니다."
        );
    }

    @GetMapping
    public RsData<List<GetRoomResponse>> findAllRooms(@PathVariable long hotelId) {
        return new RsData<>(
                "200-1",
                "모든 객실 정보를 정상적으로 조회했습니다.",
                this.roomService.findAllRooms(hotelId)
        );
    }

    @GetMapping("/{roomId}")
    public RsData<GetRoomDetailResponse> findRoomDetail(@PathVariable long hotelId, @PathVariable long roomId) {
        return new RsData<>(
                "200-1",
                "객실 정보를 정상적으로 조회했습니다.",
                this.roomService.findRoomDetail(hotelId, roomId)
        );
    }

    @GetMapping("/{roomId}/room-option")
    public RsData<GetRoomOptionResponse> findRoomOptions(@PathVariable long hotelId, @PathVariable long roomId) {
        return new RsData<>(
                "200-1",
                "객실의 옵션 정보를 정상적으로 조회했습니다.",
                this.roomService.findRoomOptions(hotelId, roomId)
        );
    }

    @PutMapping("{roomId}")
    public RsData<PutRoomResponse> modify(@PathVariable long hotelId,
                                          @PathVariable long roomId,
                                          @RequestBody PutRoomRequest request) {
        Member actor = this.rq.getActor();

        return new RsData<>(
                "200-1",
                "객실 정보를 수정에 성공하였습니다.",
                this.roomService.modifyRoom(hotelId, roomId, actor, request));
    }

    @GetMapping("/room-option")
    public RsData<GetAllRoomOptionsResponse> findAllRoomOptions(@PathVariable long hotelId) {
        Member actor = this.rq.getActor();

        return new RsData<>(
                "200-1",
                "객실 옵션 조회에 성공했습니다.",
                this.roomService.findAllRoomOptions(actor)
        );
    }
}
