package com.ll.hotel.domain.hotel.hotel.controller;

import com.ll.hotel.domain.hotel.hotel.dto.GetHotelDetailResponse;
import com.ll.hotel.domain.hotel.hotel.dto.GetHotelResponse;
import com.ll.hotel.domain.hotel.hotel.dto.PostHotelRequest;
import com.ll.hotel.domain.hotel.hotel.dto.PostHotelResponse;
import com.ll.hotel.domain.hotel.hotel.dto.PutHotelRequest;
import com.ll.hotel.domain.hotel.hotel.dto.PutHotelResponse;
import com.ll.hotel.domain.hotel.hotel.service.HotelService;
import com.ll.hotel.domain.image.type.ImageType;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.global.rq.Rq;
import com.ll.hotel.global.rsData.RsData;
import com.ll.hotel.standard.base.Empty;
import com.ll.hotel.standard.page.dto.PageDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
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
            LocalDate checkoutDate
    ) {
        return new RsData<>(
                "200-1",
                "모든 호텔 정보를 정상적으로 조회했습니다.",
                new PageDto<>(
                        this.hotelService.findAllHotels(page, pageSize, filterName, filterDirection,
                                streetAddress, checkInDate, checkoutDate))
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
}
