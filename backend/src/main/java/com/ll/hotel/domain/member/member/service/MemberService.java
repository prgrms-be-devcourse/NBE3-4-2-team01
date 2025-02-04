package com.ll.hotel.domain.member.member.service;


import com.ll.hotel.domain.member.member.dto.JoinRequest;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.entity.Role;
import com.ll.hotel.domain.member.member.repository.MemberRepository;
import com.ll.hotel.domain.member.member.type.MemberStatus;
import com.ll.hotel.global.exceptions.ServiceException;
import com.ll.hotel.global.rsData.RsData;
import com.ll.hotel.global.security.oauth2.entity.OAuth;
import com.ll.hotel.global.security.oauth2.repository.OAuthRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final OAuthRepository oAuthRepository;
    private final AuthTokenService authTokenService;
    private final RefreshTokenService refreshTokenService;
    private static final Logger log = LoggerFactory.getLogger(MemberService.class);

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
}