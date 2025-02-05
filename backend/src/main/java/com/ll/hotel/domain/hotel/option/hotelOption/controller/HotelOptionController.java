package com.ll.hotel.domain.hotel.option.hotelOption.controller;

import com.ll.hotel.domain.hotel.option.hotelOption.dto.HotelOptionDto;
import com.ll.hotel.domain.hotel.option.hotelOption.dto.request.HotelOptionRequest;
import com.ll.hotel.domain.hotel.option.hotelOption.entity.HotelOption;
import com.ll.hotel.domain.hotel.option.hotelOption.service.HotelOptionService;
import com.ll.hotel.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/hotel-amenities")
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

    @GetMapping
    public RsData<List<HotelOptionDto>> getAll() {

        List<HotelOptionDto> hotelOptionList = hotelOptionService.findAll()
                .stream()
                .map(HotelOptionDto::toDto).toList();

        return new RsData<>(
                "200",
                "모든 항목이 조회되었습니다.",
                hotelOptionList
        );
    }

    @GetMapping("/{id}")
    public RsData<HotelOptionDto> getById(@PathVariable("id") Long id) {

        HotelOption hotelOption = hotelOptionService.findById(id);

        return new RsData<>(
                "200",
                "항목이 조회되었습니다.",
                HotelOptionDto.toDto(hotelOption)
        );
    }

    @PutMapping("/{id}")
    public RsData<HotelOptionDto> modify(@PathVariable("id") Long id,
                                         @RequestBody HotelOptionRequest.Details details) {
        HotelOption hotelOption = hotelOptionService.findById(id);
        hotelOptionService.modify(hotelOption, details);

        hotelOptionService.flush();

        return new RsData<>(
                "200",
                "항목이 수정되었습니다.",
                HotelOptionDto.toDto(hotelOption)
        );
    }

    @DeleteMapping("/{id}")
    public RsData<Void> delete(@PathVariable("id") Long id) {
        HotelOption hotelOption = hotelOptionService.findById(id);
        hotelOptionService.delete(hotelOption);
        return new RsData<>(
                "200",
                "항목이 삭제되었습니다."
        );
    }
}
