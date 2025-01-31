package com.ll.hotel.domain.member.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.hotel.domain.member.member.repository.RefreshTokenRepository;
import com.ll.hotel.global.security.dto.RefreshToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    @Transactional
    public void saveTokenInfo(String email, String refreshToken, String accessToken) {
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