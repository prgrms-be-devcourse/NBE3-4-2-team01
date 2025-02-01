package com.ll.hotel.domain.hotel.room.controller;

import com.ll.hotel.domain.hotel.room.service.RoomService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hotels/{hotelId}/rooms")
@Tag(name = "RoomController", description = "객실 컨트롤러")
public class RoomController {
    private final RoomService roomService;
}
