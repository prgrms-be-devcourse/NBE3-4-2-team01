package com.ll.hotel.domain.member.member.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ll.hotel.domain.member.member.dto.MemberDTO;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.entity.Role;
import com.ll.hotel.domain.member.member.service.MemberService;
import com.ll.hotel.domain.member.member.service.RefreshTokenService;
import com.ll.hotel.domain.member.member.type.MemberStatus;
import com.ll.hotel.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class MemberController {
    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/join")
    public RsData<MemberDTO> join(@RequestBody JoinRequest joinRequest) {
        log.debug("Join attempt for email: {}", joinRequest.email());
        
        if (memberService.existsByMemberEmail(joinRequest.email())) {
            log.debug("Email already exists: {}", joinRequest.email());
            return new RsData<>("400-1", "이미 존재하는 이메일입니다.", new MemberDTO(
                null,
                joinRequest.email(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            ));
        }
        
        try {
            MemberDTO memberDTO = new MemberDTO(
                null,
                joinRequest.email(),
                joinRequest.name(),
                joinRequest.phoneNumber(),
                joinRequest.birthDate(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                Role.USER,
                MemberStatus.ACTIVE,
                joinRequest.provider()
            );
            
            Member member = memberService.join(memberDTO, joinRequest.provider());
            
            return new RsData<>(
                "200-1",
                "회원가입이 완료되었습니다.",
                new MemberDTO(
                    member.getId(),
                    member.getMemberEmail(),
                    member.getMemberName(),
                    member.getMemberPhoneNumber(),
                    member.getBirthDate(),
                    member.getCreatedAt(),
                    member.getModifiedAt(),
                    member.getRole(),
                    member.getMemberStatus(),
                    member.getProvider()
                )
            );
        } catch (Exception e) {
            log.error("Error during join process: ", e);
            return new RsData<>("400-1", "회원가입에 실패했습니다: " + e.getMessage(), null);
        }
    }

    @PostMapping("/logout")
    public RsData<String> logout(@RequestHeader("Authorization") final String accessToken) {
        try {
            if (!accessToken.startsWith("Bearer ")) {
                return new RsData<>("401-1", "잘못된 토큰 형식입니다.", "");
            }
            String token = accessToken.substring(7);
            
            if (!memberService.verifyToken(token)) {
                return new RsData<>("401-1", "Access Token 만료", "");
            }

            String email = memberService.getEmail(token);
            refreshTokenService.removeRefreshToken(email);
            return new RsData<>("200-1", "로그아웃이 완료되었습니다.", "");
        } catch (Exception e) {
            log.error("Error during logout: {}", e.getMessage());
            return new RsData<>("500-1", "로그아웃 처리 중 오류가 발생했습니다.", "");
        }
    }

    @PostMapping("/refresh")
    public RsData<String> refresh(@RequestHeader("Authorization") final String refreshToken) {
        if (!refreshToken.startsWith("Bearer ")) {
            return new RsData<>("401-1", "잘못된 토큰 형식입니다.", "");
        }
        String token = refreshToken.substring(7);
        
        return memberService.refreshAccessToken(token);
    }
}


record JoinRequest(
    String email,
    String name,
    String phoneNumber,
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate birthDate,
    String provider
) {}