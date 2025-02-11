package com.ll.hotel.domain.booking.booking.dto;

import com.ll.hotel.domain.booking.booking.entity.Booking;
import com.ll.hotel.domain.booking.booking.type.BookingStatus;
import com.ll.hotel.domain.booking.payment.dto.PaymentResponse;
import com.ll.hotel.domain.booking.payment.entity.Payment;
import com.ll.hotel.domain.hotel.hotel.dto.GetHotelResponse;
import com.ll.hotel.domain.hotel.hotel.dto.HotelDetailDto;
import com.ll.hotel.domain.hotel.hotel.dto.HotelDto;
import com.ll.hotel.domain.hotel.room.dto.GetRoomResponse;
import com.ll.hotel.domain.hotel.room.dto.RoomDto;
import com.ll.hotel.domain.member.member.dto.MemberDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public record BookingResponse(
        long bookingId,
        long roomId,
        long hotelId,
        MemberDTO member,
        PaymentResponse payment,
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
                MemberDTO.from(booking.getMember()),
                PaymentResponse.from(booking.getPayment()),
                booking.getBookingNumber(),
                booking.getBookingStatus(),
                booking.getCreatedAt(),
                booking.getModifiedAt(),
                booking.getCheckInDate(),
                booking.getCheckOutDate()
        );
    }
}