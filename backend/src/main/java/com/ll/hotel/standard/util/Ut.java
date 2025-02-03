package com.ll.hotel.standard.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.hotel.domain.member.member.service.RefreshTokenService;
import com.ll.hotel.global.app.AppConfig;
import com.ll.hotel.global.initData.BaseInit;
import com.ll.hotel.global.security.oauth2.CustomOAuth2JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

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

    public static class jwt {
        public static String toString(CustomOAuth2JwtProperties jwtProperties, Map<String, Object> claims) {
            SecretKey secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
            Date now = new Date();

            String tokenType = (String) claims.getOrDefault("type", "access");
            long expiration = tokenType.equals("refresh")
                    ? jwtProperties.getRefreshTokenExpiration()
                    : jwtProperties.getAccessTokenExpiration(); // 토큰을 타입으로 구분하여 만료 시간 설정

            return Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(now)
                    .setExpiration(new Date(now.getTime() + expiration))
                    .signWith(secretKey)
                    .compact();
        }
        public static Claims getClaims(CustomOAuth2JwtProperties jwtProperties, String token) {
            SecretKey secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
            token = token.replace("Bearer ", "").trim();

            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        }
    }
}
