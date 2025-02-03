package com.ll.hotel.domain.member.admin.dto;

import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.type.HotelStatus;

import java.time.LocalTime;

public record AdminHotelDto(
        String hotelName,
        String hotelEmail,
        String hotelPhoneNumber,
        String streetAddress,
        Integer zipCode,
        Integer hotelGrade,
        LocalTime checkInTime,
        LocalTime checkOutTime,
        String hotelExplainContent,
        HotelStatus hotelStatus
) {
    public static AdminHotelDto toDto(Hotel hotel) {
        return new AdminHotelDto(
                hotel.getHotelName(),
                hotel.getHotelEmail(),
                hotel.getHotelPhoneNumber(),
                hotel.getStreetAddress(),
                hotel.getZipCode(),
                hotel.getHotelGrade(),
                hotel.getCheckInTime(),
                hotel.getCheckOutTime(),
                hotel.getHotelExplainContent(),
                hotel.getHotelStatus()
        );
    }
}
