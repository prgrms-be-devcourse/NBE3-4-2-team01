package com.ll.hotel.global.security.dto;

import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public record OAuth2UserInfo(
        String email,
        String name,
        String provider,
        Map<String, Object>attributes,
        OAuth2User oauth2User
) {}