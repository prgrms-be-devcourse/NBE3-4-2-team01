package com.ll.hotel.domain.booking.booking.controller;

import com.ll.hotel.domain.booking.booking.dto.*;
import com.ll.hotel.domain.booking.booking.service.BookingService;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.global.rq.Rq;
import com.ll.hotel.global.rsData.RsData;
import com.ll.hotel.standard.base.Empty;
import com.ll.hotel.standard.page.dto.PageDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final Rq rq;

    // 예약 페이지 정보 요청
    @GetMapping
    public RsData<BookingFormResponse> preBook(
            @RequestParam(name = "hotelId") long hotelId,
            @RequestParam(name = "roomId") long roomId) {
        Member actor = rq.getActor();

        return new RsData<>(
                "200",
                "예약 페이지 정보 조회에 성공했습니다.",
                bookingService.preCreate(hotelId, roomId, actor)
        );
    }

    // 예약 및 결제
    @PostMapping
    public RsData<Empty> book(
            @RequestBody @Valid BookingRequest bookingRequest) {
        Member actor = rq.getActor();
        bookingService.create(actor, bookingRequest);

        return RsData.OK;
    }

    // 사용자측 예약 조회 (사업자, 관리자도 조회는 가능)
    @GetMapping("/me")
    public RsData<PageDto<BookingResponseSummary>> getMyBookings(
            @RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(defaultValue = "5", name = "page_size") int pageSize) {
        Member actor = rq.getActor();

        return new RsData<>(
                "200",
                "내 예약 목록 조회에 성공했습니다.",
                new PageDto<>(bookingService.tryGetMyBookings(actor, page, pageSize))
        );
    }

    // 호텔(사업자)측 예약 조회
    @GetMapping("/myHotel")
    public RsData<PageDto<BookingResponseSummary>> getHotelBookings(
            @RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(defaultValue = "5", name = "page_size") int pageSize) {
        Member actor = rq.getActor();

        return new RsData<>(
                "200",
                "호텔 예약 목록 조회에 성공했습니다.",
                new PageDto<>(bookingService.tryGetHotelBookings(actor, page, pageSize))
        );
    }

    // 예약 상세 조회
    @GetMapping("/{booking_id}")
    public RsData<BookingResponseDetails> getBookingDetails(
            @PathVariable("booking_id") long bookingId) {
        Member actor = rq.getActor();

        return new RsData<>(
                "200",
                "예약 상세 조회에 성공했습니다.",
                bookingService.tryGetBookingDetails(actor, bookingId)
        );
    }

    // 예약 취소
    @DeleteMapping("/{booking_id}")
    public RsData<Empty> cancel(
            @PathVariable("booking_id") long bookingId) {
        Member actor = rq.getActor();
        bookingService.tryCancel(actor, bookingId);

        return RsData.OK;
    }

    // 예약 완료 처리
    @PostMapping("/{booking_id}")
    public RsData<Empty> complete(
            @PathVariable("booking_id") long bookingId) {
        Member actor = rq.getActor();
        bookingService.trySetCompleted(actor, bookingId);

        return RsData.OK;
    }
}
