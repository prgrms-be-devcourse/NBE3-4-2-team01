package com.ll.hotel.global.security.dto;

import lombok.Builder;

@Builder
public record SecurityUserDto(
        String email,
        String nickname,
        String role,
        Long memberNo // 세션 정보 저장을 위한 회원 번호
) {}