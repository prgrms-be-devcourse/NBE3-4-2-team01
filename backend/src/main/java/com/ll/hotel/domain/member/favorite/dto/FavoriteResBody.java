package com.ll.hotel.domain.member.favorite.dto;


public record FavoriteResBody(
        FavoriteDTO favorite,
        String message
) {}