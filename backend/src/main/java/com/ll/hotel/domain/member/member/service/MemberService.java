package com.ll.hotel.domain.member.member.service;


import com.ll.hotel.domain.member.member.dto.JoinRequest;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.entity.Role;
import com.ll.hotel.domain.member.member.repository.MemberRepository;
import com.ll.hotel.domain.member.member.type.MemberStatus;
import com.ll.hotel.global.exceptions.ServiceException;
import com.ll.hotel.global.rsData.RsData;
import com.ll.hotel.global.security.oauth2.CustomOAuth2JwtProperties;
import com.ll.hotel.global.security.oauth2.entity.OAuth;
import com.ll.hotel.global.security.oauth2.repository.OAuthRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final OAuthRepository oAuthRepository;
    private final AuthTokenService authTokenService;
    private final RefreshTokenService refreshTokenService;
    private final RedisTemplate<String, String> redisTemplate;
    private final CustomOAuth2JwtProperties jwtProperties;
    private static final Logger log = LoggerFactory.getLogger(MemberService.class);
    private static final String LOGOUT_PREFIX = "LOGOUT:";

    @Transactional
    public Member join(JoinRequest joinRequest) {
        log.debug("Starting join process for email: {}", joinRequest.email());
        
        if (memberRepository.existsByMemberEmail(joinRequest.email())) {
            throw new ServiceException("400-1", "이미 가입된 이메일입니다.");
        }

        log.debug("Building member entity with provider: {}, oauthId: {}", 
                 joinRequest.provider(), joinRequest.oauthId());

        Member member = Member.builder()
                .memberEmail(joinRequest.email())
                .memberName(joinRequest.name())
                .memberPhoneNumber(joinRequest.phoneNumber())
                .birthDate(joinRequest.birthDate())
                .role(Role.USER)
                .memberStatus(MemberStatus.ACTIVE)
                .password("")
                .build();

        log.debug("Attempting to save member entity");
        member = memberRepository.save(member);
        log.debug("Successfully saved member entity with id: {}", member.getId());

        if (joinRequest.provider() != null && joinRequest.oauthId() != null) {
            log.debug("Creating OAuth entity - provider: {}, oauthId: {}", 
                     joinRequest.provider(), joinRequest.oauthId());
                     
            OAuth oauth = OAuth.create(
                member,
                joinRequest.provider(),
                joinRequest.oauthId()
            );
            oAuthRepository.save(oauth);
            log.debug("Successfully saved OAuth entity");
        }

        return member;
    }

    public Optional<Member> findByMemberEmail(String email) {
        return memberRepository.findByMemberEmail(email);
    }

    public boolean existsByMemberEmail(String email) {
        log.debug("Checking if member exists with email: {}", email);
        boolean exists = memberRepository.existsByMemberEmail(email);
        log.debug("Member exists with email {}: {}", email, exists);
        return exists;
    }

    public boolean verifyToken(String accessToken) {
        return authTokenService.verifyToken(accessToken);
    }

    public String getEmail(String accessToken) {
        return authTokenService.getEmail(accessToken);
    }

    public RsData<String> refreshAccessToken(String refreshToken) {
        return refreshTokenService.refreshAccessToken(refreshToken);
    }

    public String generateRefreshToken(String email) {
        return refreshTokenService.generateRefreshToken(email);
    }

    public void logout(String accessToken) {
        if (!StringUtils.hasText(accessToken)) {
            throw new ServiceException("400-1", "토큰이 필요합니다.");
        }

        String email = authTokenService.getEmail(accessToken);
        
        redisTemplate.opsForValue().set(
            LOGOUT_PREFIX + accessToken,
            email,
            jwtProperties.getAccessTokenExpiration(),
            TimeUnit.MILLISECONDS
        );

        refreshTokenService.removeRefreshToken(email);
    }

    public boolean isLoggedOut(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(LOGOUT_PREFIX + token));
    }

    public String getEmailFromToken(String token) {
        return authTokenService.getEmail(token);
    }

    public String extractEmailIfValid(String token) {
        if (isLoggedOut(token)) {
            throw new ServiceException("401-1", "로그아웃된 토큰입니다.");
        }
        if (!verifyToken(token)) {
            throw new ServiceException("401-2", "유효하지 않은 토큰입니다.");
        }
        return getEmailFromToken(token);
    }
}