package com.ll.hotel.domain.hotel.amenity.roomAmenity.controller;

import com.ll.hotel.domain.hotel.amenity.roomAmenity.dto.RoomAmenityDTO;
import com.ll.hotel.domain.hotel.amenity.roomAmenity.dto.request.RoomAmenityRequest;
import com.ll.hotel.domain.hotel.amenity.roomAmenity.entity.RoomAmenity;
import com.ll.hotel.domain.hotel.amenity.roomAmenity.service.RoomAmenityService;
import com.ll.hotel.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/room_amenities")
@RequiredArgsConstructor
public class RoomAmenityController {
    private final RoomAmenityService roomAmenityService;

    @PostMapping
    public RsData<RoomAmenityDTO> add(@RequestBody @Valid RoomAmenityRequest.Details details) {

        RoomAmenity roomAmenity = roomAmenityService.add(details);

        return new RsData<>(
                "201",
                "'항목이 추가되었습니다.",
                RoomAmenityDTO.toDTO(roomAmenity)
        );
    }

    @GetMapping
    public RsData<List<RoomAmenityDTO>> getAll() {

        List<RoomAmenityDTO> roomAmenityList = roomAmenityService.findAll()
                .stream()
                .map(RoomAmenityDTO::toDTO).toList();

        return new RsData<>(
                "200",
                "모든 항목이 조회되었습니다.",
                roomAmenityList
        );
    }

    @GetMapping("/{id}")
    public RsData<RoomAmenityDTO> getById(@PathVariable("id") Long id) {

        RoomAmenity roomAmenity = roomAmenityService.findById(id);

        return new RsData<>(
                "200",
                "항목이 조회되었습니다.",
                RoomAmenityDTO.toDTO(roomAmenity)
        );
    }
}
