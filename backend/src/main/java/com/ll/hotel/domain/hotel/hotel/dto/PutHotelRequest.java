package com.ll.hotel.domain.hotel.hotel.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import org.hibernate.validator.constraints.Length;

public record PutHotelRequest(
        @NotBlank @Length(min = 2, max = 30)
        String hotelName,

        @Email @NotBlank
        String hotelEmail,

        @NotBlank @Pattern(regexp = "^01[0-9]-\\d{4}-\\d{4}$", message = "Invalid phone number format")
        String hotelPhoneNumber,

        @NotBlank
        String streetAddress,

        @NotNull
        Integer zipCode,

        @NotNull @Min(value = 1) @Max(value = 5)
        Integer hotelGrade,

        @NotNull
        LocalTime checkInTime,

        @NotNull
        LocalTime checkOutTime,

        @NotBlank
        String hotelExplainContent,

        @NotBlank
        String hotelStatus,

        @NotNull
        List<String> hotelImages,

        @NotNull
        Set<String> hotelOptions
) {
}
