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
@RequestMapping("/api/bookings/")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    //private final Rq rq;

    @PostMapping
    @Transactional
    public RsData<BookingResponse> book(
            @RequestBody @Valid BookingRequest bookingRequest) {
        //Member member = rq.findByActor().get();
        Member member = new Member();

        Booking booking = bookingService.create(member, bookingRequest);

        return new RsData<>(
                "201",
                String.format("%s번 예약이 완료되었습니다.", booking.getBookingNumber()),
                BookingResponse.from(booking)
        );
    }

    @GetMapping("/me")
    @Transactional
    public RsData<PageDto<BookingResponse>> getMyBookings(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int pageSize) {
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

    @GetMapping("/hotels/{hotel_id}")
    @Transactional
    public RsData<PageDto<BookingResponse>> getHotelBookings(
            @PathVariable Long hotelId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int pageSize) {
        return new RsData<>(
                "200",
                "호텔 예약 목록 조회에 성공했습니다.",
                new PageDto<>(
                        bookingService.findByHotelId(hotelId, page, pageSize)
                                .map(BookingResponse::from)
                )
        );
    }

    @GetMapping("/{book_id}")
    @Transactional
    public RsData<BookingResponse> getBooking(
            @PathVariable Long bookId) {
        Booking booking = bookingService.findById(bookId).orElseThrow(
                () -> new ServiceException("404", "예약 정보를 찾을 수 없습니다.")
        );

        return new RsData<>(
                "200",
                "예약 상세 조회에 성공했습니다.",
                BookingResponse.from(booking)
        );
    }

    @DeleteMapping("/{book_id}")
    @Transactional
    public RsData<Empty> cancel(
            @PathVariable Long bookId) {
        Booking booking = bookingService.findById(bookId).orElseThrow(
                () -> new ServiceException("404", "예약 정보를 찾을 수 없습니다.")
        );

        bookingService.cancel(booking);

        return new RsData<>(
                "200",
                String.format("%s번 예약이 취소되었습니다.", booking.getBookingNumber())
        );
    }
}
