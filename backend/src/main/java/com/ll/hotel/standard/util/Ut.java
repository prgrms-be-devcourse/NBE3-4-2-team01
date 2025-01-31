package com.ll.hotel.standard.util;

import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.hotel.domain.member.member.service.RefreshTokenService;
import com.ll.hotel.global.app.AppConfig;
import com.ll.hotel.global.initData.BaseInit;
import com.ll.hotel.global.security.dto.GeneratedToken;
import com.ll.hotel.global.security.dto.SecurityUserDto;
import com.ll.hotel.global.security.oauth2.CustomOAuth2JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class Ut {

    private final CustomOAuth2JwtProperties jwtProperties;
    private final RefreshTokenService tokenService;
    private final BaseInit baseInit;

    public static class str {
        public static boolean isBlank(String str) {
            return str == null || str.trim().isEmpty();
        }
    }

    public static class json {
        private static final ObjectMapper om = AppConfig.getObjectMapper();

        @SneakyThrows
        public static String toString(Object obj) {
            return om.writeValueAsString(obj);
        }
    }


    public GeneratedToken generateToken(String email, String role) {
        String refreshToken = generateRefreshToken(email, role);
        String accessToken = generateAccessToken(email, role);

        // Redis 사용, 토큰 저장
        tokenService.saveTokenInfo(email, refreshToken, accessToken);
        return new GeneratedToken(accessToken, refreshToken);
    }

    public String generateRefreshToken(String email, String role) {
        long refreshPeriod = jwtProperties.getRefreshTokenExpiration();

        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshPeriod))
                .signWith(baseInit.getSecretKey())
                .compact();
    }


    public String generateAccessToken(String email, String role) {
        long tokenPeriod = jwtProperties.getAccessTokenExpiration();
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);

        Date now = new Date();
        return
                Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + tokenPeriod))
                        .signWith(baseInit.getSecretKey())
                        .compact();

    }


    public boolean verifyToken(String token) {
        try {
            // Bearer 접두사 제거
            token = token.replace("Bearer ", "");

            log.debug("Verifying token: {}", token);

            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(baseInit.getSecretKey())
                    .build()
                    .parseClaimsJws(token);

            Date expiration = claims.getBody().getExpiration();
            Date now = new Date();

            log.debug("Token expiration: {}, Current time: {}", expiration, now);

            return expiration.after(now);
        } catch (Exception e) {
            log.error("Token verification failed: {}", e.getMessage());
            return false;
        }
    }

    // Email 추출
    public String getUid(String token) {
        // Bearer 접두사 제거
        token = token.replace("Bearer ", "");
        return Jwts.parserBuilder().setSigningKey(baseInit.getSecretKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    // Role(권한) 추출
    public String getRole(String token) {
        // Bearer 접두사 제거
        token = token.replace("Bearer ", "");
        return Jwts.parserBuilder().setSigningKey(baseInit.getSecretKey()).build().parseClaimsJws(token).getBody().get("role", String.class);
    }

    public static SecurityUserDto getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUserDto securityUserDto) {
            return securityUserDto;
        }
        return null;
    }
}
