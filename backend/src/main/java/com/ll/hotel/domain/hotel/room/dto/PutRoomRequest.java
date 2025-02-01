package com.ll.hotel.domain.hotel.room.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import org.hibernate.validator.constraints.Length;

public record PutRoomRequest(

        @NotBlank @Length(min = 2, max = 30) String roomName,

        @NotNull @Min(value = 0) Integer roomNumber,

        @NotNull Integer basePrice,

        @NotNull @Min(value = 1) Integer standardNumber,

        @NotNull @Min(value = 1) Integer maxNumber,

        @NotBlank String bedTypeNumber,

        @NotNull List<String> roomImages,

        @NotNull Set<String> roomOptions
) {
}
