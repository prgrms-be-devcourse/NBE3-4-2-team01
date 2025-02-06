package com.ll.hotel.global.security.dto;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 86400) // 1일 후 만료
public class RefreshToken implements Serializable {

    @Id
    private String id;  // email을 id로 사용

    @Indexed  // refreshToken으로 조회하기 위해 인덱스 추가
    private String refreshToken;

    private String accessToken;

    // 기본 생성자 추가
    protected RefreshToken() {
    }

    // 모든 필드를 포함하는 생성자
    public RefreshToken(String id, String refreshToken, String accessToken) {
        this.id = id;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    public void updateAccessToken(String accessToken) {
        if (!StringUtils.hasText(accessToken)) {
            throw new IllegalArgumentException("액세스 토큰은 null 이거나 비어 있을 수 없습니다");
        }
        this.accessToken = accessToken;
    }

}
