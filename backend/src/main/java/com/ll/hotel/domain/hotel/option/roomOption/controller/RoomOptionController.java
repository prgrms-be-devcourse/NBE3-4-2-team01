package com.ll.hotel.domain.hotel.option.roomOption.controller;

import com.ll.hotel.domain.hotel.option.roomOption.dto.RoomOptionDto;
import com.ll.hotel.domain.hotel.option.roomOption.dto.request.RoomOptionRequest;
import com.ll.hotel.domain.hotel.option.roomOption.entity.RoomOption;
import com.ll.hotel.domain.hotel.option.roomOption.service.RoomOptionService;
import com.ll.hotel.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public RsData<List<RoomOptionDto>> getAll() {

        List<RoomOptionDto> roomAmenityList = roomOptionService.findAll()
                .stream()
                .map(RoomOptionDto::toDto).toList();

        return new RsData<>(
                "200",
                "모든 항목이 조회되었습니다.",
                roomAmenityList
        );
    }

    @GetMapping("/{id}")
    public RsData<RoomOptionDto> getById(@PathVariable("id") Long id) {

        RoomOption roomOption = roomOptionService.findById(id);

        return new RsData<>(
                "200",
                "항목이 조회되었습니다.",
                RoomOptionDto.toDto(roomOption)
        );
    }

    @PutMapping("/{id}")
    public RsData<RoomOptionDto> modify(@PathVariable("id") Long id,
                                        @RequestBody RoomOptionRequest.Details details) {
        RoomOption roomOption = roomOptionService.findById(id);
        roomOptionService.modify(roomOption, details);

        roomOptionService.flush();

        return new RsData<>(
                "200",
                "항목이 수정되었습니다.",
                RoomOptionDto.toDto(roomOption)
        );
    }
}
