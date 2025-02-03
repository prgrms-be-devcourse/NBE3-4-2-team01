package com.ll.hotel.global.security.dto;

import lombok.Builder;

@Builder
public record GeneratedToken(
        String accessToken,
        String refreshToken
) {}