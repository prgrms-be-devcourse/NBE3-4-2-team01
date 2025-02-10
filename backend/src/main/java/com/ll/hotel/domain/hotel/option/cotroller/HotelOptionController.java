package com.ll.hotel.domain.hotel.option.cotroller;

import com.ll.hotel.domain.hotel.option.dto.request.OptionRequest;
import com.ll.hotel.domain.hotel.option.dto.response.OptionResponse;
import com.ll.hotel.domain.hotel.option.entity.HotelOption;
import com.ll.hotel.domain.hotel.option.service.HotelOptionService;
import com.ll.hotel.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/hotel-options")
@RequiredArgsConstructor
public class HotelOptionController {
    private final HotelOptionService hotelOptionService;

    @PostMapping
    public RsData<OptionResponse> add(@RequestBody @Valid OptionRequest optionRequest) {

        HotelOption hotelOption = hotelOptionService.add(optionRequest);

        return new RsData<>(
                "201",
                "항목이 추가되었습니다.",
                OptionResponse.from(hotelOption)
        );
    }

    @GetMapping
    public RsData<List<OptionResponse>> getAll() {

        List<OptionResponse> hotelOptionList = hotelOptionService.findAll()
                .stream()
                .map(OptionResponse::from).toList();

        return new RsData<>(
                "200",
                "모든 항목이 조회되었습니다.",
                hotelOptionList
        );
    }

    @GetMapping("/{id}")
    public RsData<OptionResponse> getById(@PathVariable("id") Long id) {

        HotelOption hotelOption = hotelOptionService.findById(id);

        return new RsData<>(
                "200",
                "항목이 조회되었습니다.",
                OptionResponse.from(hotelOption)
        );
    }

    @PatchMapping("/{id}")
    public RsData<OptionResponse> modify(@PathVariable("id") Long id,
                                         @RequestBody OptionRequest optionRequest) {
        HotelOption hotelOption = hotelOptionService.findById(id);
        hotelOptionService.modify(hotelOption, optionRequest);

        hotelOptionService.flush();

        return new RsData<>(
                "200",
                "항목이 수정되었습니다.",
                OptionResponse.from(hotelOption)
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
