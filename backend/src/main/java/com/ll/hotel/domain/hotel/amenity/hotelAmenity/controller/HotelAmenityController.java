package com.ll.hotel.domain.hotel.amenity.hotelAmenity.controller;

import com.ll.hotel.domain.hotel.amenity.hotelAmenity.dto.HotelAmenityDTO;
import com.ll.hotel.domain.hotel.amenity.hotelAmenity.dto.request.HotelAmenityRequest;
import com.ll.hotel.domain.hotel.amenity.hotelAmenity.entity.HotelAmenity;
import com.ll.hotel.domain.hotel.amenity.hotelAmenity.service.HotelAmenityService;
import com.ll.hotel.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public RsData<List<HotelAmenityDTO>> getAll() {

        List<HotelAmenityDTO> hotelAmenityList = hotelAmenityService.findAll()
                .stream()
                .map(HotelAmenityDTO::toDTO).toList();

        return new RsData<>(
                "200",
                "모든 항목이 조회되었습니다.",
                hotelAmenityList
        );
    }

    @GetMapping("/{id}")
    public RsData<HotelAmenityDTO> getById(@PathVariable("id") Long id) {

        HotelAmenity hotelAmenity = hotelAmenityService.findById(id);

        return new RsData<>(
                "200",
                "항목이 조회되었습니다.",
                HotelAmenityDTO.toDTO(hotelAmenity)
        );
    }

    @PutMapping("/{id}")
    public RsData<HotelAmenityDTO> modify(@PathVariable("id") Long id,
                                               @RequestBody HotelAmenityRequest.Details details) {
        HotelAmenity hotelAmenity = hotelAmenityService.findById(id);
        hotelAmenityService.modify(hotelAmenity, details);

        hotelAmenityService.flush();

        return new RsData<>(
                "200",
                "항목이 수정되었습니다.",
                HotelAmenityDTO.toDTO(hotelAmenity)
        );
    }

    @DeleteMapping("/{id}")
    public RsData<Void> delete(@PathVariable("id") Long id) {
        HotelAmenity hotelAmenity = hotelAmenityService.findById(id);
        hotelAmenityService.delete(hotelAmenity);
        return new RsData<>(
                "200",
                "'%s' 항목이 삭제되었습니다.".formatted(hotelAmenity.getDescription())
        );
    }
}
