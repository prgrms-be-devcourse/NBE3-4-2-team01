package com.ll.hotel.domain.booking.booking.controller;

import com.ll.hotel.domain.booking.booking.dto.BookingRequest;
import com.ll.hotel.domain.booking.booking.dto.BookingResponse;
import com.ll.hotel.domain.booking.booking.entity.Booking;
import com.ll.hotel.domain.booking.booking.service.BookingService;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.global.exceptions.ServiceException;
import com.ll.hotel.global.rq.Rq;
import com.ll.hotel.global.rsData.RsData;
import com.ll.hotel.standard.base.Empty;
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
    //private final Rq rq;

    // 예약
    @PostMapping
    @Transactional
    public RsData<BookingResponse> book(
            @RequestBody @Valid BookingRequest bookingRequest) {
        //Member member = rq.findByActor().get();
        Member member = new Member();
        System.out.println(bookingRequest);
        Booking booking = bookingService.create(member, bookingRequest);

        return new RsData<>(
                "201",
                String.format("%s번 예약이 완료되었습니다.", booking.getBookingNumber()),
                BookingResponse.from(booking)
        );
    }

    // 사용자측 예약 조회
    @GetMapping("/me")
    @Transactional
    public RsData<PageDto<BookingResponse>> getMyBookings(
            @RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(defaultValue = "5", name = "page_size") int pageSize) {
        //Member member = rq.findByActor().get();
        Member member = new Member();

        return new RsData<>(
                "200",
                "내 예약 목록 조회에 성공했습니다.",
                new PageDto<>(
                        bookingService.findByMember(member, page, pageSize)
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
        Booking booking = bookingService.findById(bookingId);

        return new RsData<>(
                "200",
                "예약 상세 조회에 성공했습니다.",
                BookingResponse.from(booking)
        );
    }

    // 예약 취소
    @DeleteMapping("/{booking_id}")
    @Transactional
    public RsData<Booking> cancel(
            @PathVariable("booking_id") Long bookingId) {
        Booking booking = bookingService.findById(bookingId);
        bookingService.cancel(booking);

        return new RsData<>(
                "200",
                String.format("%s번 예약이 취소되었습니다.", booking.getBookingNumber()),
                booking
        );
    }
}
