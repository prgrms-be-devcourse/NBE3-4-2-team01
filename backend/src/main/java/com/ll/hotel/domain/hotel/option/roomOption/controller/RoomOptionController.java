package com.ll.hotel.domain.hotel.option.roomOption.controller;

import com.ll.hotel.domain.hotel.option.roomOption.dto.RoomOptionDto;
import com.ll.hotel.domain.hotel.option.roomOption.dto.request.RoomOptionRequest;
import com.ll.hotel.domain.hotel.option.roomOption.entity.RoomOption;
import com.ll.hotel.domain.hotel.option.roomOption.service.RoomOptionService;
import com.ll.hotel.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/room-amenities")
@RequiredArgsConstructor
public class RoomOptionController {
    private final RoomOptionService roomOptionService;

    @PostMapping
    public RsData<RoomOptionDto> add(@RequestBody @Valid RoomOptionRequest.Details details) {

        RoomOption roomOption = roomOptionService.add(details);

        return new RsData<>(
                "201",
                "'항목이 추가되었습니다.",
                RoomOptionDto.toDto(roomOption)
        );
    }
}
