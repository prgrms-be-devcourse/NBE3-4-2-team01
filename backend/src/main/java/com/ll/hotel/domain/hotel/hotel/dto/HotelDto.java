package com.ll.hotel.domain.hotel.hotel.dto;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.option.entity.HotelOption;

import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

public record HotelDto(
        long hotelId,

        @NotBlank
        String hotelName,

        @NotBlank
        String hotelEmail,

        @NotBlank
        String hotelPhoneNumber,

        @NotBlank
        String streetAddress,

        @NonNull
        Integer zipCode,

        @NonNull
        Integer hotelGrade,

        @NonNull
        LocalTime checkInTime,

        @NonNull
        LocalTime checkOutTime,

        @NotBlank
        String hotelExplainContent,

        @NotBlank
        String hotelStatus,

        List<?> rooms,

        Set<String> hotelOptions

) {
    public HotelDto(Hotel hotel) {
        this(
                hotel.getId(),
                hotel.getHotelName(),
                hotel.getHotelEmail(),
                hotel.getHotelPhoneNumber(),
                hotel.getStreetAddress(),
                hotel.getZipCode(),
                hotel.getHotelGrade(),
                hotel.getCheckInTime(),
                hotel.getCheckOutTime(),
                hotel.getHotelExplainContent(),
                hotel.getHotelStatus().getValue(),
                hotel.getRooms(),
                hotel.getHotelOptions() != null
                        ? hotel.getHotelOptions().stream()
                        .map(HotelOption::getName)
                        .collect(Collectors.toSet())
                        : new HashSet<>()
        );
    }
    
    // 즐겨찾기 목록 조회 간소화 DTO
    public HotelDto(
            long hotelId,
            String hotelName,
            String hotelEmail,
            String hotelPhoneNumber,
            String streetAddress,
            Integer zipCode,
            Integer hotelGrade,
            LocalTime checkInTime,
            LocalTime checkOutTime,
            String hotelExplainContent,
            String hotelStatus,
            List<Map<String, Object>> simplifiedRooms,
            List<String> hotelOptionNames
    ) {
        this(
                hotelId,
                hotelName,
                hotelEmail,
                hotelPhoneNumber,
                streetAddress,
                zipCode,
                hotelGrade,
                checkInTime,
                checkOutTime,
                hotelExplainContent,
                hotelStatus,
                simplifiedRooms,
                hotelOptionNames != null
                        ? new HashSet<>(hotelOptionNames)
                        : new HashSet<>()
        );
    }
}
