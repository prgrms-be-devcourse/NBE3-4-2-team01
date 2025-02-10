package com.ll.hotel.domain.hotel.hotel.controller;

import com.ll.hotel.domain.hotel.hotel.dto.*;
import com.ll.hotel.domain.hotel.hotel.service.HotelService;
import com.ll.hotel.domain.image.type.ImageType;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.global.exceptions.ServiceException;
import com.ll.hotel.global.rq.Rq;
import com.ll.hotel.global.rsData.RsData;
import com.ll.hotel.global.validation.GlobalValidation;
import com.ll.hotel.standard.base.Empty;
import com.ll.hotel.standard.page.dto.PageDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hotels")
@Tag(name = "HotelController", description = "호텔 컨트롤러")
public class HotelController {
    private final HotelService hotelService;
    private final Rq rq;

    @PostMapping
    public RsData<PostHotelResponse> create(@RequestBody @Valid PostHotelRequest postHotelRequest) {
        Member actor = this.rq.getActor();

        return new RsData<>(
                "201-1",
                "호텔을 정상적으로 등록하였습니다.",
                this.hotelService.createHotel(actor, postHotelRequest)
        );
    }

    @PostMapping("/{hotelId}/urls")
    public RsData<Empty> saveImageUrls(@PathVariable long hotelId,
                                       @RequestBody List<String> urls
    ) {
        Member actor = this.rq.getActor();

        this.hotelService.saveImages(actor, ImageType.HOTEL, hotelId, urls);

        return new RsData<>(
                "201-1",
                "호텔 이미지 저장에 성공하였습니다."
        );
    }

    @GetMapping
    public RsData<PageDto<GetHotelResponse>> findAllHotels(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "latest") String filterName,
            @RequestParam(required = false) String filterDirection,
            @RequestParam(defaultValue = "") String streetAddress,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now()}") @DateTimeFormat(iso = ISO.DATE)
            LocalDate checkInDate,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().plusDays(1)}") @DateTimeFormat(iso = ISO.DATE)
            LocalDate checkoutDate,
            @RequestParam(defaultValue = "2") int personal
    ) {
        GlobalValidation.checkPageSize(pageSize);
        GlobalValidation.checkCheckInAndOutDate(checkInDate, checkoutDate);

        return new RsData<>(
                "200-1",
                "모든 호텔 정보를 정상적으로 조회했습니다.",
                new PageDto<>(
                        this.hotelService.findAllHotels(page, pageSize, filterName, filterDirection,
                                streetAddress, checkInDate, checkoutDate, personal))
        );
    }

    @GetMapping("/{hotelId}")
    public RsData<GetHotelDetailResponse> findHotelDetail(@PathVariable long hotelId) {
        return new RsData<>(
                "200-1",
                "호텔 정보를 정상적으로 조회했습니다.",
                this.hotelService.findHotelDetail(hotelId)
        );
    }

    @PutMapping("/{hotelId}")
    public RsData<PutHotelResponse> modifyHotel(@PathVariable long hotelId,
                                                @RequestBody @Valid PutHotelRequest request
    ) {
        Member actor = this.rq.getActor();

        return new RsData<>(
                "200-1",
                "호텔 정보 수정에 성공하였습니다.",
                this.hotelService.modifyHotel(hotelId, actor, request)
        );
    }

    @DeleteMapping("/{hotelId}")
    public RsData<Empty> deleteHotel(@PathVariable Long hotelId) {
        Member actor = this.rq.getActor();

        this.hotelService.deleteHotel(hotelId, actor);

        return new RsData<>(
                "200-1",
                "호텔 삭제에 성공하였습니다."
        );
    }

    @GetMapping("{hotelId}/revenue")
    public RsData<GetHotelRevenueResponse> findHotelRevenue(@PathVariable long hotelId) {
        Member actor = this.rq.getActor();

        if (actor == null) {
            throw new ServiceException("401-1", "로그인 해주세요.");
        }

        if (!actor.isBusiness()) {
            throw new ServiceException("403-1", "사업가만 호텔 매출을 확인할 수 있습니다.");
        }

        return new RsData<>(
                "200-1",
                "호텔 매출 조회에 성공하였습니다.",
                this.hotelService.findRevenue(hotelId, actor)
        );
    }

    @GetMapping("/hotel-option")
    public RsData<GetAllHotelOptionsResponse> findAllHotelOptions() {
        Member actor = this.rq.getActor();

        return new RsData<>(
                "200-1",
                "호텔 옵션 조회에 성공했습니다.",
                this.hotelService.findHotelOptions(actor)
        );
    }
}
