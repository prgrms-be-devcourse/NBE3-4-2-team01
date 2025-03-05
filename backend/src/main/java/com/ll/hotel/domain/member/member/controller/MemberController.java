package com.ll.hotel.domain.member.member.controller;

import com.ll.hotel.domain.member.member.dto.JoinRequest;
import com.ll.hotel.domain.member.member.dto.MemberDTO;
import com.ll.hotel.domain.member.member.dto.MemberResponse;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.service.MemberService;
import com.ll.hotel.global.exceptions.ServiceException;
import com.ll.hotel.global.rsData.RsData;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

import static com.ll.hotel.global.exceptions.ErrorCode.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/join")
    public RsData<MemberResponse> join(@RequestBody @Valid JoinRequest joinRequest, 
                                     HttpServletResponse response,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

            throw EMAIL_ALREADY_EXISTS.throwServiceException();
        }
        
        try {
            Member member = memberService.join(joinRequest);
            
            // OAuth2 회원가입인 경우 자동 로그인 처리
            if (joinRequest.provider() != null && joinRequest.oauthId() != null) {
                memberService.oAuth2Login(member, response);
            }
            
            MemberResponse memberResponse = new MemberResponse(
                MemberDTO.from(member),
                member.getMemberEmail()
            );
            return RsData.success(HttpStatus.OK, memberResponse);
        } catch (ServiceException e) {
            throw EMAIL_ALREADY_EXISTS.throwServiceException();
        }
    }

    @PostMapping("/logout")
    public RsData<?> logout(HttpServletRequest request, HttpServletResponse response) {
        memberService.logout(request, response);
        return RsData.success(HttpStatus.OK, null);
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
            throw REFRESH_TOKEN_NOT_FOUND.throwServiceException();
        }
        
        return memberService.refreshAccessToken(refreshToken);
    }
}