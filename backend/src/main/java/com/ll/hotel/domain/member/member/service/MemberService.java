package com.ll.hotel.domain.member.member.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ll.hotel.domain.member.member.dto.MemberDTO;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.entity.Role;
import com.ll.hotel.domain.member.member.repository.MemberRepository;
import com.ll.hotel.domain.member.member.type.MemberStatus;
import com.ll.hotel.global.rsData.RsData;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthTokenService authTokenService;
    private final RefreshTokenService refreshTokenService;
    private static final Logger log = LoggerFactory.getLogger(MemberService.class);

    public Member join(MemberDTO memberDTO, String provider) {
        Member createdMember = Member.builder()
                .memberEmail(memberDTO.memberEmail())
                .password("")
                .memberName(memberDTO.memberName())
                .memberPhoneNumber(memberDTO.memberPhoneNumber())
                .birthDate(memberDTO.birthDate())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .role(Role.USER)
                .memberStatus(MemberStatus.ACTIVE)
                .provider(provider)
                .build();

        return memberRepository.save(createdMember);
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