package com.ll.hotel.domain.member.member.service;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.hotel.domain.member.member.repository.RefreshTokenRepository;
import static com.ll.hotel.global.exceptions.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.ll.hotel.global.exceptions.ErrorCode.REFRESH_TOKEN_INVALID;
import static com.ll.hotel.global.exceptions.ErrorCode.REFRESH_TOKEN_NOT_FOUND;
import static com.ll.hotel.global.exceptions.ErrorCode.TOKEN_INVALID;
import com.ll.hotel.global.jwt.dto.JwtProperties;
import com.ll.hotel.global.jwt.dto.RefreshToken;
import com.ll.hotel.global.response.RsData;
import com.ll.hotel.standard.util.Ut;

import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final JwtProperties jwtProperties;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    void saveTokenInfo(String email, String refreshToken, String accessToken) {
        try {
            log.debug("Saving token for email: {}", email);
            RefreshToken token = new RefreshToken(email, refreshToken, accessToken);
            repository.save(token);

            String key = "rt:refreshToken:" + refreshToken;
            redisTemplate.expire(key, 86400, TimeUnit.SECONDS);
            log.debug("Token saved successfully for email: {}", email);
        } catch (Exception e) {
            log.error("Failed to save token", e);
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
        try {
            String token = refreshToken.replace("Bearer ", "").trim();

            String tokenType = Ut.jwt.getClaims(jwtProperties, token).get("type", String.class);
            if (!"refresh".equals(tokenType)) {
                REFRESH_TOKEN_INVALID.throwServiceException();
            }

            boolean tokenExists = repository.existsByRefreshToken(token);
            log.debug("Found refresh token in repository: {}", tokenExists);

            if (!tokenExists) {
                REFRESH_TOKEN_NOT_FOUND.throwServiceException();
            }
            RefreshToken resultToken = repository.findByRefreshToken(token)
                .orElseThrow(REFRESH_TOKEN_NOT_FOUND::throwServiceException);

            String email = resultToken.getId();
            String role = Ut.jwt.getClaims(jwtProperties, token).get("role", String.class);
            String newAccessToken = Ut.jwt.toString(jwtProperties, Map.of("sub", email, "role", role));

            resultToken.updateAccessToken(newAccessToken);
            repository.save(resultToken);

            return RsData.success(HttpStatus.OK, newAccessToken);
        } catch (MalformedJwtException e) {
            throw TOKEN_INVALID.throwServiceException();
        } catch (Exception e) {
            log.error("토큰 갱신 중 오류 발생", e);
            throw INTERNAL_SERVER_ERROR.throwServiceException();
        }
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