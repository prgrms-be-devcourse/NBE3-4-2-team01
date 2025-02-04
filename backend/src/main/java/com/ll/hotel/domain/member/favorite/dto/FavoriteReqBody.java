package com.ll.hotel.domain.member.favorite.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FavoriteReqBody(
    @NotNull(message = "호텔 ID는 필수입니다.")
    @Positive(message = "호텔 ID는 양수여야 합니다.")
    @Min(value = 1, message = "호텔 ID는 1 이상이어야 합니다.")
    Long hotelId
) {}
