package com.ll.hotel.domain.member.favorite.dto;

public record FavoriteReqBody(
    long hotelId,
    long memberId
) {}
