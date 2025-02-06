package com.ll.hotel.domain.member.member.controller;

import com.ll.hotel.domain.member.member.dto.JoinRequest;
import com.ll.hotel.domain.member.member.dto.MemberDTO;
import com.ll.hotel.domain.member.member.dto.MemberResponse;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.service.MemberService;
import com.ll.hotel.domain.member.member.service.RefreshTokenService;
import com.ll.hotel.global.exceptions.ServiceException;
import com.ll.hotel.global.rsData.RsData;
import com.ll.hotel.global.security.oauth2.dto.SecurityUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class MemberController {
    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/join")
    public RsData<MemberResponse> join(@RequestBody @Valid JoinRequest joinRequest, 
                                     @AuthenticationPrincipal SecurityUser securityUser) {
        log.debug("Join attempt for email: {}", joinRequest.email());
        try {
            if (securityUser != null && securityUser.getOauthId() != null) {
                log.debug("SecurityUser OAuth 정보 - provider: {}, oauthId: {}", 
                         securityUser.getProvider(), securityUser.getOauthId());
                         
                joinRequest = new JoinRequest(
                    joinRequest.email(),
                    joinRequest.name(),
                    joinRequest.phoneNumber(),
                    joinRequest.birthDate(),
                    securityUser.getProvider(),
                    securityUser.getOauthId()
                );
            }
            
            Member member = memberService.join(joinRequest);
            MemberResponse response = new MemberResponse(
                MemberDTO.from(member),
                member.getMemberEmail()
            );
            return new RsData<>("200", "회원가입이 완료되었습니다.", response);
        } catch (ServiceException e) {
            return new RsData<>("400-1", e.getMessage(), new MemberResponse(null, null));
        }
    }

    @PostMapping("/logout")
    public RsData<Void> logout(@RequestHeader("Authorization") String bearerToken) {
        if (!StringUtils.hasText(bearerToken) || !bearerToken.startsWith("Bearer ")) {
            throw new ServiceException("400-1", "잘못된 토큰 형식입니다.");
        }

        String token = bearerToken.substring(7);
        memberService.logout(token);
        
        return new RsData<>("200-1", "로그아웃 되었습니다.");
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