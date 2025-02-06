package com.ll.hotel.domain.member.member.service;


import com.ll.hotel.domain.member.member.repository.RefreshTokenRepository;
import com.ll.hotel.global.rsData.RsData;
import com.ll.hotel.global.security.dto.RefreshToken;
import com.ll.hotel.global.security.oauth2.CustomOAuth2JwtProperties;
import com.ll.hotel.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final CustomOAuth2JwtProperties jwtProperties;

    @Transactional
    void saveTokenInfo(String email, String refreshToken, String accessToken) {
        try {
            log.debug("Saving token for email: {}", email);
            RefreshToken token = new RefreshToken(email, refreshToken, accessToken);
            repository.save(token);
            log.debug("Token saved successfully for email: {}", email);
        } catch (Exception e) {
            log.error("Failed to save token for email: {}", email, e);
            throw e;
        }
    }

    String generateRefreshToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", email);
        claims.put("role", "ROLE_USER");
        claims.put("type", "refresh");
        return Ut.jwt.toString(jwtProperties, claims);
    }

    @Transactional
    RsData<String> refreshAccessToken(String refreshToken) {
        String token = refreshToken.replace("Bearer ", "").trim();
        
        String tokenType = Ut.jwt.getClaims(jwtProperties, token).get("type", String.class);
        if (!"refresh".equals(tokenType)) {
            return new RsData<>("401-1", "리프레시 토큰이 아닙니다.", "");
        }

        boolean tokenExists = repository.existsByRefreshToken(token);
        log.debug("Found refresh token in repository: {}", tokenExists);

        if (tokenExists) {
            RefreshToken resultToken = repository.findByRefreshToken(token)
                .orElseThrow(() -> new RuntimeException("토큰을 찾을 수 없습니다."));
            
            String email = resultToken.getId();
            String role = Ut.jwt.getClaims(jwtProperties, token).get("role", String.class);
            String newAccessToken = Ut.jwt.toString(jwtProperties, Map.of("sub", email, "role", role));
            
            resultToken.updateAccessToken(newAccessToken);
            repository.save(resultToken);
            
            return new RsData<>("200-1", "토큰이 갱신되었습니다.", newAccessToken);
        }

        return new RsData<>("400-1", "유효하지 않은 리프레시 토큰입니다.", "");
    }

    @Transactional
    public void removeRefreshToken(String email) {
        try {
            log.debug("Attempting to remove token for email: {}", email);
            if (repository.existsById(email)) {
                repository.deleteById(email);
                log.debug("Token removed successfully for email: {}", email);
            } else {
                log.debug("No token found for email: {}", email);
                // 토큰이 없어도 오류로 처리하지 않음
            }
        } catch (Exception e) {
            log.error("Failed to remove token for email: {}", email, e);
            throw e;
        }
    }
}