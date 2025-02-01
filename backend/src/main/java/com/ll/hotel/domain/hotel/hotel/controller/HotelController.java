package com.ll.hotel.domain.hotel.hotel.controller;

import com.ll.hotel.domain.hotel.hotel.dto.PostHotelRequest;
import com.ll.hotel.domain.hotel.hotel.dto.PostHotelResponse;
import com.ll.hotel.domain.hotel.hotel.service.HotelService;
import com.ll.hotel.global.rsData.RsData;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hotels")
@Tag(name = "HotelController", description = "호텔 컨트롤러")
public class HotelController {
    private final HotelService hotelService;

    @PostMapping
    public RsData<PostHotelResponse> create(@RequestBody @Valid PostHotelRequest postHotelRequest) {
        return new RsData<>(
                "201-1",
                "호텔을 정상적으로 등록하였습니다.",
                this.hotelService.create(postHotelRequest)
        );
    }
}
