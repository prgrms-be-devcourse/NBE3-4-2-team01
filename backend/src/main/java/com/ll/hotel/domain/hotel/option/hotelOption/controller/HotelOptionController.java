package com.ll.hotel.domain.hotel.option.hotelOption.controller;

import com.ll.hotel.domain.hotel.option.hotelOption.dto.HotelOptionDto;
import com.ll.hotel.domain.hotel.option.hotelOption.dto.request.HotelOptionRequest;
import com.ll.hotel.domain.hotel.option.hotelOption.entity.HotelOption;
import com.ll.hotel.domain.hotel.option.hotelOption.service.HotelOptionService;
import com.ll.hotel.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/hotel-amenities")
@RequiredArgsConstructor
public class HotelOptionController {
    private final HotelOptionService hotelOptionService;

    @PostMapping
    public RsData<HotelOptionDto> add(@RequestBody @Valid HotelOptionRequest.Details details) {

        HotelOption hotelOption = hotelOptionService.add(details);

        return new RsData<>(
                "201",
                "항목이 추가되었습니다.",
                HotelOptionDto.toDto(hotelOption)
        );
    }
}
