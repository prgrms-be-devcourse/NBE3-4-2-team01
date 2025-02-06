package com.ll.hotel.global.security.oauth2.repository;

import com.ll.hotel.global.security.oauth2.entity.OAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthRepository extends JpaRepository<OAuth, Long> {
    Optional<OAuth> findByProviderAndOauthId(String provider, String oauthId);
} 