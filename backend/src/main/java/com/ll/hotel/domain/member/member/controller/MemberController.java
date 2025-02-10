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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class MemberController {
    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/join")
    public RsData<MemberResponse> join(@RequestBody @Valid JoinRequest joinRequest, 
                                     @AuthenticationPrincipal SecurityUser securityUser,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
                
            return new RsData<>("400", errorMessage, null);
        }
        
        try {
            if (securityUser != null && securityUser.getOauthId() != null) {
                log.debug("SecurityUser OAuth 정보 - provider: {}, oauthId: {}", 
                         securityUser.getProvider(), securityUser.getOauthId());
                         
                joinRequest = new JoinRequest(
                    joinRequest.email(),
                    joinRequest.name(),
                    joinRequest.phoneNumber(),
                    joinRequest.role(),
                    securityUser.getProvider(),
                    securityUser.getOauthId(),
                    joinRequest.birthDate()
                );
            }
            
            Member member = memberService.join(joinRequest);
            MemberResponse response = new MemberResponse(
                MemberDTO.from(member),
                member.getMemberEmail()
            );
            return new RsData<>("200", "회원가입이 완료되었습니다.", response);
        } catch (ServiceException e) {
            return new RsData<>("400", "이미 가입된 이메일입니다. 소셜 로그인이 연동되었습니다.", 
                new MemberResponse(null, joinRequest.email()));
        }
    }

    @PostMapping("/logout")
    public RsData<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        memberService.logout(request, response);
        return new RsData<>("200-1", "로그아웃 되었습니다.");
    }

    @PostMapping("/refresh")
    public RsData<String> refresh(HttpServletRequest request) {
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        
        if (refreshToken == null) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                refreshToken = authHeader.substring(7);
            }
        }
        
        if (refreshToken == null) {
            return new RsData<>("401-1", "리프레시 토큰이 없습니다.", null);
        }
        
        return memberService.refreshAccessToken(refreshToken);
    }
}