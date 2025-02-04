package com.ll.hotel.domain.hotel.hotel.controller;

import com.ll.hotel.domain.hotel.hotel.dto.GetHotelResponse;
import com.ll.hotel.domain.hotel.hotel.dto.HotelDto;
import com.ll.hotel.domain.hotel.hotel.dto.PostHotelRequest;
import com.ll.hotel.domain.hotel.hotel.dto.PostHotelResponse;
import com.ll.hotel.domain.hotel.hotel.dto.PutHotelRequest;
import com.ll.hotel.domain.hotel.hotel.dto.PutHotelResponse;
import com.ll.hotel.domain.hotel.hotel.service.HotelService;
import com.ll.hotel.global.rsData.RsData;
import com.ll.hotel.standard.base.Empty;
import com.ll.hotel.standard.page.dto.PageDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping
    public RsData<PageDto<GetHotelResponse>> findAllHotels(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "latest") String filterName,
            @RequestParam(required = false) String filterDirection
    ) {
        return new RsData<>(
                "200-1",
                "호텔 리스트를 정상적으로 조회했습니다.",
                new PageDto<>(
                        this.hotelService.findAll(page, pageSize, filterName, filterDirection))
        );
    }

    @GetMapping("/{hotelId}")
    public RsData<HotelDto> findHotelDetail(@PathVariable long hotelId) {
        return new RsData<>(
                "200-1",
                "호텔 정보를 정상적으로 불러왔습니다.",
                this.hotelService.findHotelDetail(hotelId)
        );
    }

    @PutMapping("/{hotelId}")
    public RsData<PutHotelResponse> modifyHotel(@PathVariable long hotelId,
                                                @RequestBody @Valid PutHotelRequest request) {
        return new RsData<>(
                "200-1",
                "호텔 정보 수정에 성공하였습니다.",
                this.hotelService.modify(hotelId, request)
        );
    }

    @DeleteMapping("/{hotelId}")
    public RsData<Empty> deleteHotel(@PathVariable Long hotelId) {
        this.hotelService.delete(hotelId);

        return new RsData<>(
                "200-1",
                "호텔 삭제에 성공하였습니다."
        );
    }
}
