package com.ll.hotel.domain.booking.booking.dto;

import java.time.LocalDate;

public record BookingRequest(
        Long roomId,
        Long hotelId,
        Long paymentId,
        LocalDate checkInDate,
        LocalDate checkOutDate
) {
}
