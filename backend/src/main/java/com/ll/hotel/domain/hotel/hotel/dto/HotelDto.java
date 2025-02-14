package com.ll.hotel.domain.hotel.hotel.dto;

import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.option.entity.HotelOption;
import com.ll.hotel.domain.hotel.room.entity.Room;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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

        List<Room> rooms,

        Set<String> hotelOptions

        // 호텔 Favorite
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
}
