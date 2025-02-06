package com.ll.hotel.domain.booking.booking.dto;

import com.ll.hotel.domain.booking.booking.entity.Booking;
import com.ll.hotel.domain.booking.booking.type.BookingStatus;
import com.ll.hotel.domain.booking.payment.entity.Payment;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BookingResponse(
        Long bookingId,
        Long roomId,
        Long hotelId,
        Long memberId,
        Payment payment,
        String bookNumber,
        BookingStatus bookingStatus,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        LocalDate checkInDate,
        LocalDate checkOutDate
) {
    public static BookingResponse from(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getRoom().getId(),
                booking.getHotel().getId(),
                booking.getMember().getId(),
                booking.getPayment(),
                booking.getBookingNumber(),
                booking.getBookingStatus(),
                booking.getCreatedAt(),
                booking.getModifiedAt(),
                booking.getCheckInDate(),
                booking.getCheckOutDate()
        );
    }
}