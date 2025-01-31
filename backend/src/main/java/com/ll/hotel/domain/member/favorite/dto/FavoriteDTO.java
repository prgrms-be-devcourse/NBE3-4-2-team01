package com.ll.hotel.domain.member.favorite.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FavoriteDTO {
    private String hotelId;
    private String customerId;
} 