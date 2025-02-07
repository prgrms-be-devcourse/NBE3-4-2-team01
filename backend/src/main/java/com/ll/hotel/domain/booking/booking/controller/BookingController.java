package com.ll.hotel.domain.booking.booking.controller;

import com.ll.hotel.domain.booking.booking.dto.BookingRequest;
import com.ll.hotel.domain.booking.booking.dto.BookingResponse;
import com.ll.hotel.domain.booking.booking.entity.Booking;
import com.ll.hotel.domain.booking.booking.service.BookingService;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.global.exceptions.ServiceException;
import com.ll.hotel.global.rq.Rq;
import com.ll.hotel.global.rsData.RsData;
import com.ll.hotel.standard.page.dto.PageDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final Rq rq;

    // 예약 및 결제
    @PostMapping
    @Transactional
    public RsData<BookingResponse> book(
            @RequestBody @Valid BookingRequest bookingRequest) {
        Member actor = rq.getActor();

        // 사용자 정보가 없으면 예약 불가
        if (actor == null) {
            throw new ServiceException("401", "예약 권한이 없습니다.");
        }

        Booking booking = bookingService.create(actor, bookingRequest);

        return new RsData<>(
                "201",
                "예약 및 결제에 성공하였습니다.",
                BookingResponse.from(booking)
        );
    }

    // 전체 예약 조회
    @GetMapping
    @Transactional
    public RsData<PageDto<BookingResponse>> getAllBookings(
            @RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(defaultValue = "5", name = "page_size") int pageSize) {
        Member actor = rq.getActor();

        // 관리자만 조회 가능
        if (actor == null || !(actor.isAdmin())) {
            throw new ServiceException("401", "예약 조회 권한이 없습니다.");
        }

        return new RsData<>(
                "200",
                "호텔 예약 목록 조회에 성공했습니다.",
                new PageDto<>(
                        bookingService.findAll(page, pageSize)
                                .map(BookingResponse::from)
                )
        );
    }

    // 사용자측 예약 조회
    @GetMapping("/me")
    @Transactional
    public RsData<PageDto<BookingResponse>> getMyBookings(
            @RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(defaultValue = "5", name = "page_size") int pageSize) {
        Member actor = rq.getActor();

        // 사용자 정보가 없으면 조회 불가
        if (actor == null) {
            throw new ServiceException("401", "예약 조회 권한이 없습니다.");
        }

        return new RsData<>(
                "200",
                "내 예약 목록 조회에 성공했습니다.",
                new PageDto<>(
                        bookingService.findByMember(actor, page, pageSize)
                                .map(BookingResponse::from)
                )
        );
    }

    // 호텔측 예약 조회
    @GetMapping("/hotels/{hotel_id}")
    @Transactional
    public RsData<PageDto<BookingResponse>> getHotelBookings(
            @PathVariable("hotel_id") Long hotelId,
            @RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(defaultValue = "5", name = "page_size") int pageSize) {
        Member actor = rq.getActor();

        // 관리자, 호텔 사업자만 조회 가능
        if (actor == null || !(actor.isAdmin() || actor.isBusiness())) {
            throw new ServiceException("401", "예약 조회 권한이 없습니다.");
        }

        return new RsData<>(
                "200",
                "호텔 예약 목록 조회에 성공했습니다.",
                new PageDto<>(
                        bookingService.findByHotelId(hotelId, page, pageSize)
                                .map(BookingResponse::from)
                )
        );
    }

    // 예약 상세 조회
    @GetMapping("/{booking_id}")
    @Transactional
    public RsData<BookingResponse> getBooking(
            @PathVariable("booking_id") Long bookingId) {
        Member actor = rq.getActor();
        Booking booking = bookingService.findById(bookingId);

        // 관리자, 예약자, 호텔 사업자만 조회 가능
        if (actor == null || !(actor.isAdmin() || booking.isReservedBy(actor) || booking.isOwnedBy(actor))) {
            throw new ServiceException("401", "예약 조회 권한이 없습니다.");
        }

        return new RsData<>(
                "200",
                "예약 상세 조회에 성공했습니다.",
                BookingResponse.from(booking)
        );
    }

    // 예약 취소
    @DeleteMapping("/{booking_id}")
    @Transactional
    public RsData<BookingResponse> cancel(
            @PathVariable("booking_id") Long bookingId) {
        Member actor = rq.getActor();
        Booking booking = bookingService.findById(bookingId);

        // 관리자, 예약자, 호텔 사업자만 취소 가능
        if (actor == null || !(actor.isAdmin() || booking.isReservedBy(actor) || booking.isOwnedBy(actor))) {
            throw new ServiceException("401", "예약 취소 권한이 없습니다.");
        }

        booking = bookingService.cancel(booking);

        return new RsData<>(
                "200",
                "예약 및 결제가 취소되었습니다.",
                BookingResponse.from(booking)
        );
    }
}
