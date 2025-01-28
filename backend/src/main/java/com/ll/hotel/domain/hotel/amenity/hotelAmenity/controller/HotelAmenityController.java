package com.ll.hotel.domain.hotel.amenity.hotelAmenity.controller;

import com.ll.hotel.domain.hotel.amenity.hotelAmenity.dto.HotelAmenityDTO;
import com.ll.hotel.domain.hotel.amenity.hotelAmenity.dto.request.HotelAmenityRequest;
import com.ll.hotel.domain.hotel.amenity.hotelAmenity.entity.HotelAmenity;
import com.ll.hotel.domain.hotel.amenity.hotelAmenity.service.HotelAmenityService;
import com.ll.hotel.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/amenities")
@RequiredArgsConstructor
public class HotelAmenityController {
    private final HotelAmenityService hotelAmenityService;

    @PostMapping
    public RsData<HotelAmenityDTO> add(@RequestBody @Valid HotelAmenityRequest.Details details) {

        HotelAmenity hotelAmenity = hotelAmenityService.add(details);

        return new RsData<>(
                "201",
                "'항목이 추가되었습니다.",
                HotelAmenityDTO.toDTO(hotelAmenity)
        );
    }
}
